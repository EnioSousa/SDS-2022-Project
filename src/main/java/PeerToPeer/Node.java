package PeerToPeer;

import BlockChain.HashAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

// TODO: Improve the contact list. Maybe use a timer to erase useless entries

public class Node {
    /**
     * Logger object to log stuff
     */
    public static final Logger LOGGER = LogManager.getLogger(Node.class);
    /**
     * Info of the current node
     */
    private final NodeInfo nodeInfo;
    /**
     * Server running on the node
     */
    private final NodeServer nodeServer;
    /**
     * List of connections to other nodes
     */
    private final ConcurrentLinkedQueue<NodeClient> nodeClients;
    /**
     * K buckets that is running this node
     */
    private final K_Buckets kBuckets;
    /**
     * This var will be used for the find node. We have async calls, so
     * we need a way to check if we have contacted a given node
     */
    private final HashMap<byte[], LinkedList<NodeInfo>> findNodeContactedList =
            new HashMap<>();
    /**
     * This var will be used for the finds. We have async calls, so we need a
     * way to know if for a given find call we have already contacted a node
     */
    private final HashMap<byte[], LinkedList<NodeInfo>> findContactedList =
            new HashMap<>();
    /**
     * This var will be used to save the values that we find
     */
    private final HashMap<byte[], byte[]> keyValuePair =
            new HashMap<>();
    /**
     * Holds the stored values
     */
    private final StoredValues storedValues;

    /**
     * Node known to all
     */
    public static NodeInfo knownNode =
            new NodeInfo(new byte[]{0x00}, "localhost", 5000);

    public Node(NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;

        LOGGER.info("New Node: " + nodeInfo);

        nodeServer = new NodeServer(this, nodeInfo.getPort());
        nodeServer.start();

        nodeClients = new ConcurrentLinkedQueue<>();

        //TODO: Change id and key size. They have to be the same
        kBuckets = new K_Buckets(this, 8, 4);
        storedValues = new StoredValues(8, this);

        if (!nodeInfo.equals(knownNode)) {
            getKBuckets().addNodeInfo(knownNode);
        }
    }

    /**
     * Store a keyPair in our node
     *
     * @param key   The key
     * @param value The value
     * @return True if the key, value pair was saved
     */
    public boolean storeValue(byte[] key, byte[] value) {
        return storedValues.storeValue(key, value);
    }

    /**
     * Get a value from a keyPair from our node
     *
     * @param key The key
     * @return The value if it exists, otherwise null
     */
    public byte[] getStoredValue(byte[] key) {
        return storedValues.getStoredValue(key);
    }

    /**
     * This method starts the find call. The find call is async
     *
     * @param key The key we want to search
     */
    public void doFind(byte[] key) {
        try {
            byte[] normalizedKey = HashAlgorithm.generateHash(key,
                    getKBuckets().getSpaceSize());

            keyValuePair.remove(normalizedKey);

            byte[] entry = HashAlgorithm.generateHash(normalizedKey);

            findContactedList.remove(entry);
            findContactedList.put(entry, new LinkedList<>());

            LinkedList<NodeInfo> list =
                    getKBuckets().getKClosest(normalizedKey);

            for (NodeInfo nodeInfo : list) {
                getNodeClient(nodeInfo).doFind(normalizedKey, 0, entry);
            }

        } catch (Exception e) {
            LOGGER.error("Hash error: " + e);
        }
    }

    /**
     * This method does a find to a specific node. Because by definition the
     * find call is recursive, i.e. it will continue to do finds until a
     * searched value is found, we need to set a limit to the number of
     * calls, with alpha, and a way to know if we already contacted a given
     * node, we can do this by having a list of contacts, identified by the
     * entry parameter
     *
     * @param nodeInfo      The node to contact
     * @param normalizedKey The normalized key. See kadamlia for more info
     * @param alpha         The maximum number of recursion depth
     * @param entry         The entry identifying the already contacted group
     */
    void doFind(NodeInfo nodeInfo, byte[] normalizedKey, int alpha,
                byte[] entry) {
        if (alpha >= 3 || (findContactedList.get(entry) != null &&
                findContactedList.get(entry).contains(nodeInfo))) {
            return;
        }

        findContactedList.get(entry).add(nodeInfo);

        getNodeClient(nodeInfo).doFind(normalizedKey, alpha, entry);
    }

    /**
     * This method does the store. It will get the k closest nodes, and try
     * to store a keyValue pair in those nodes.
     *
     * @param key   The key
     * @param value The value
     */
    public void doStore(byte[] key, byte[] value) {
        try {
            byte[] normalizedKey = HashAlgorithm.generateHash(key,
                    getKBuckets().getSpaceSize());

            LinkedList<NodeInfo> list =
                    getKBuckets().getKClosest(normalizedKey);

            for (NodeInfo nodeInfo : list) {
                NodeClient nodeClient = getNodeClient(nodeInfo);

                nodeClient.doStore(normalizedKey, value);
            }

        } catch (Exception e) {
            LOGGER.error("Hash error: " + e);
        }
    }

    /**
     * This method will start a find node request. The find node request,
     * will start by getting the k closest nodes to a given id, and for each
     * subsequent response, will try to contact the new nodes for the
     * wanted id
     *
     * @param wantedId the id we want to find
     * @throws NoSuchAlgorithmException If hash algorithm is not found
     */
    public void doFindNode(byte[] wantedId) throws NoSuchAlgorithmException {
        LinkedList<NodeInfo> contactList = getKBuckets().getKClosest(wantedId);

        LOGGER.info(contactList);

        byte[] entry = HashAlgorithm.generateHash(wantedId);

        // TODO: Improve how we check the already contacted nodes
        findNodeContactedList.remove(entry);

        findNodeContactedList.put(entry, new LinkedList<>(contactList));

        for (NodeInfo nodeInfo : contactList) {
            NodeClient nodeClient = getNodeClient(nodeInfo);

            nodeClient.doFindNode(wantedId, 0, entry);
        }
    }

    /**
     * Do find request to a given node. Similar idea to doFind/3
     *
     * @param nodeInfo The node to do the request
     * @param wantedId The wanted id
     * @param alpha    The current alpha
     * @param entry    The entry of the contacted list
     */
    void doFindNode(NodeInfo nodeInfo, byte[] wantedId, int alpha, byte[] entry) {
        // If the node hasn't been contacted, then..
        if (nodeInfo.equals(getNodeInfo())) {
            return;
        } else if (findNodeContactedList.get(entry) != null &&
                findNodeContactedList.get(entry).contains(nodeInfo)) {
            return;
        } else {
            findNodeContactedList.get(entry).add(nodeInfo);
        }

        NodeClient nodeClient = getNodeClient(nodeInfo);

        nodeClient.doFindNode(wantedId, alpha, entry);
    }

    /**
     * This method is responsible for adding the requester info to the k
     * buckets, or in case it's already there, put him at the end of the list
     *
     * @param nodeInfo the node that made us a request
     */
    public void gotRequest(NodeInfo nodeInfo) {
        if (!nodeInfo.equals(getNodeInfo())) {
            getKBuckets().addNodeInfo(nodeInfo);
        }
    }

    /**
     * The method is responsible for adding the node that responded to us,
     * to the k buckets list, or to the end of the list
     *
     * @param nodeInfo The node that answered us
     */
    public void gotResponse(NodeInfo nodeInfo) {
        gotRequest(nodeInfo);
    }

    /**
     * When a given k bucket is full, we will try to replace the oldest non-speaking
     * node, by trying to ping him. If he doesn't respond we eliminate him
     *
     * @param newInfo THe node candidate to be the replacement
     * @param oldInfo The node candidate to be replaced
     */
    void kBucketFullDoPing(NodeInfo newInfo, NodeInfo oldInfo) {
        if (!newInfo.equals(oldInfo)) {
            NodeClient nodeClient = getNodeClient(oldInfo);

            nodeClient.kBucketFullDoPing(newInfo, oldInfo);
        }
    }

    /**
     * Do a sync ping to a node. If ping fails we will try to remove the
     * connection from our database
     *
     * @param nodeInfo The respective node info
     * @return True if ping successful, otherwise false
     */
    boolean doPingSync(NodeInfo nodeInfo) {
        NodeClient nodeClient = getNodeClient(nodeInfo);

        boolean success = nodeClient.doPingSync();

        if (!success) {
            nodeClient.closeClient();
            nodeClients.remove(nodeClient);
        }

        return success;
    }

    /**
     * Add new node client to our clients list
     *
     * @param nodeClient New node client to add
     * @return True if successfully added
     */
    private boolean addNodeClient(NodeClient nodeClient) {
        boolean success = nodeClients.add(nodeClient);

        if (success) {
            LOGGER.info("Adding new node to list: " + nodeClient);
        } else {
            LOGGER.info("Failed to add new node to list: " + nodeClient);
        }

        return success;
    }

    /**
     * Get Node client from the info of a node. If the client already exist,
     * then it returns the respective node client, otherwise a new NodeClient
     *
     * @param nodeInfo the info of a node
     * @return The node client, if it exists, otherwise creates a new one
     */
    private NodeClient getNodeClient(NodeInfo nodeInfo) {
        for (NodeClient nodeClient : nodeClients) {
            if (nodeClient.getConnectedNodeInfo().equals(nodeInfo)) {
                return nodeClient;
            }
        }

        return connectToNodeWithoutPing(nodeInfo);
    }

    /**
     * Create a grpc channel and stubs, without making ping afterwards
     *
     * @param nodeInfo The node we want to connect
     * @return The node client
     */
    NodeClient connectToNodeWithoutPing(NodeInfo nodeInfo) {
        for (NodeClient nodeClient : nodeClients) {
            if (nodeClient.equals(nodeInfo)) {
                LOGGER.info("Already connected: " + nodeInfo);
                return nodeClient;
            }
        }

        NodeClient nodeClient = new NodeClient(this, nodeInfo);

        addNodeClient(nodeClient);

        return nodeClient;
    }

    /**
     * Get the current node info
     *
     * @return NodeInfo object containing the info of the node running
     */
    public NodeInfo getNodeInfo() {
        return nodeInfo;
    }

    /**
     * Get the server this node is running
     *
     * @return NodeServer Object that this node is running
     */
    public NodeServer getNodeServer() {
        return nodeServer;
    }

    /**
     * Return the k buckets from this node
     *
     * @return The k buckets
     */
    public K_Buckets getKBuckets() {
        return kBuckets;
    }

    @Override
    public String toString() {
        return getNodeInfo().toString();
    }
}
