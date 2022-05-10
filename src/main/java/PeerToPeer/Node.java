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
    private HashMap<byte[], LinkedList<NodeInfo>> findNodeContactedList =
            new HashMap<>();
    /**
     * This var will be used for the finds. We have async calls, so we need a
     * way to know if for a given find call we have already contacted a node
     */
    private HashMap<byte[], LinkedList<NodeInfo>> findContactedList =
            new HashMap<>();
    /**
     * This var will be used to save the values that we find
     */
    private HashMap<byte[], byte[]> keyValuePair =
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
        kBuckets = new K_Buckets(nodeInfo, 8, 4);
        storedValues = new StoredValues(8, this);

        if (!nodeInfo.equals(knownNode)) {
            getKBuckets().addNodeInfo(knownNode);
        }
    }

    /**
     * Store given value in our node
     *
     * @param key   The key
     * @param value The value
     * @return True if the key, value pair was saved
     */
    public boolean storeValue(byte[] key, byte[] value) {
        return storedValues.storeValue(key, value);
    }

    public byte[] getStoredValue(byte[] key) {
        return storedValues.getStoredValue(key);
    }

    public HashMap<byte[], byte[]> getKeyValuePair() {
        return keyValuePair;
    }

    public void doFind(byte[] key) {
        try {
            byte[] normalizedKey = HashAlgorithm.generateHash(key,
                    getKBuckets().getSpaceSize());

            keyValuePair.remove(normalizedKey);

            byte[] entry = HashAlgorithm.generateHash(normalizedKey);

            LinkedList<NodeInfo> list =
                    getKBuckets().getKClosest(normalizedKey);

            for (NodeInfo nodeInfo : list) {
                getNodeClient(nodeInfo).doFind(normalizedKey, 0, entry);
            }

        } catch (Exception e) {
            LOGGER.error("Hash error: " + e);
        }
    }

    public void doFind(NodeInfo nodeInfo, byte[] normalizedKey, int alpha,
                       byte[] entry) {
        if (alpha >= 3 || (findContactedList.get(entry) != null &&
                findContactedList.get(entry).contains(nodeInfo))) {
            return;
        }

        findContactedList.get(entry).add(nodeInfo);

        getNodeClient(nodeInfo).doFind(normalizedKey, alpha, entry);
    }

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
     * Return the contacted list of nodes from the find node call
     *
     * @param entry The entry identifying the contact group
     * @return A list of nodes already contacted
     */
    public LinkedList<NodeInfo> getFindNodeContactedList(byte[] entry) {
        return findNodeContactedList.get(entry);
    }

    /**
     * Return the contacted list of nodes from the find call
     *
     * @param entry The entry identifying the contact group
     * @return A list of nodes already contacted
     */
    public LinkedList<NodeInfo> getFindContactedList(byte[] entry) {
        return findContactedList.get(entry);
    }

    /**
     * Remove entry from the contacted list
     *
     * @param entry The entry to remove
     * @return True if removed, otherwise false
     */
    public boolean removeEntryFromFindNodeContactList(byte[] entry) {
        return findNodeContactedList.remove(entry) != null;
    }

    /**
     * Remove a given entry from the contact list from the find call
     *
     * @param entry The entry identifying the call group
     * @return True if removed, otherwise false
     */
    public boolean removeEntryFromFindContactList(byte[] entry) {
        return findContactedList.remove(entry) != null;
    }

    /**
     * Add new contact to our contacted list.
     *
     * @param entry    The entry identifying the contact group
     * @param nodeInfo The node info to add
     * @return True if the contact was added, otherwise false
     */
    @SuppressWarnings("DuplicatedCode")
    public boolean addToFindNodeContactList(byte[] entry, NodeInfo nodeInfo) {
        if (findNodeContactedList.containsKey(entry)) {
            return findNodeContactedList.get(entry).add(nodeInfo);
        } else {
            LinkedList<NodeInfo> list = new LinkedList<>();
            list.add(nodeInfo);

            return findNodeContactedList.put(entry, list) != null;
        }
    }

    @SuppressWarnings("DuplicatedCode")
    public boolean addToFindContactList(byte[] entry, NodeInfo nodeInfo) {
        if (findContactedList.containsKey(entry)) {
            return findContactedList.get(entry).add(nodeInfo);
        } else {
            LinkedList<NodeInfo> list = new LinkedList<>();
            list.add(nodeInfo);

            return findContactedList.put(entry, list) != null;
        }
    }

    /**
     * This method will start a find node request. The find node request,
     * will start by getting the k closest nodes to a given id, and for each
     * subsequent response, we will try to contact the new nodes for the
     * wanted id
     *
     * @param wantedId the id we want to find
     * @throws NoSuchAlgorithmException
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
     * Do find request to a given node.
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
     * This method is responsible for adding the requester info to the k buckets
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
     * to the k buckets list
     *
     * @param nodeInfo The node that answered us
     */
    public void gotResponse(NodeInfo nodeInfo) {
        gotRequest(nodeInfo);
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
     * @param nodeClient New object to add
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
     * then it return the respective nodeclient, otherwise a new NodeClient
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
     * Try to connect to node. First we try to create the rpc channels, and
     * then try to ping the node. If everything is successful then we add
     * the node to our node list
     *
     * @param nodeInfo The info of the node we want to connect
     * @return True if we successfully connected to the node
     */
    boolean connectToNode(NodeInfo nodeInfo) {
        if (nodeClients.contains(nodeInfo)) {
            LOGGER.info("Already connected: " + nodeInfo);
            return true;
        } else {
            NodeClient nodeClient = new NodeClient(this, nodeInfo);

            boolean success = nodeClient.doPingSync();

            if (success) {
                success = addNodeClient(nodeClient);
            }

            return success;
        }
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
