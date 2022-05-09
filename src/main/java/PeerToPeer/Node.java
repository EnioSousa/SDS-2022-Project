package PeerToPeer;

import BlockChain.HashAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

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
     * This var will be used for the find node stuff
     */
    private HashMap<byte[], LinkedList<NodeInfo>> contactedList =
            new HashMap<>();
    /**
     * Holds the stored values
     */
    private final StoredValues storedValues;

    public Node(NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;

        LOGGER.info("New Node: " + nodeInfo);

        nodeServer = new NodeServer(this, nodeInfo.getPort());
        nodeServer.start();

        nodeClients = new ConcurrentLinkedQueue<>();

        //TODO: Change id and key size. They have to be the same
        kBuckets = new K_Buckets(nodeInfo, 8, 4);
        storedValues = new StoredValues(8, this);

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

    public void doFindValue(byte[] key) {
        try {
            byte[] normalizedKey = HashAlgorithm.generateHash(key,
                    getKBuckets().getSpaceSize());


        } catch (Exception e) {
            LOGGER.error("Hash error: " + e);
        }
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
     * Return the contacted list of nodes
     *
     * @param entry The entry identifying the contact group
     * @return A list of nodes already contacted
     */
    public LinkedList<NodeInfo> getContactedList(byte[] entry) {
        return contactedList.get(entry);
    }

    /**
     * Remove entry from the contacted list
     *
     * @param entry The entry to remove
     * @return True if removed, otherwise false
     */
    public boolean removeEntryFromContactList(byte[] entry) {
        return contactedList.remove(entry) != null ? true : false;
    }

    /**
     * Add new contact to our contacted list.
     *
     * @param entry    The entry identifying the contact group
     * @param nodeInfo The node info to add
     * @return True if the contact was added, otherwise false
     */
    public boolean addToContactList(byte[] entry, NodeInfo nodeInfo) {
        return contactedList.get(entry).add(nodeInfo);
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

        byte[] entry = HashAlgorithm.generateHash(wantedId);

        // TODO: Improve how we check the already contacted nodes
        if (contactedList.containsKey(entry)) {
            contactedList.remove(entry);
        }
        contactedList.put(entry, new LinkedList<>(contactList));

        for (NodeInfo nodeInfo : contactList) {
            NodeClient nodeClient = getNodeClient(nodeInfo);

            if (nodeClient == null) {
                connectToNode(nodeInfo);
            }

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
        if (contactedList.get(entry) != null && contactedList.get(entry).contains(nodeInfo)) {
            return;
        } else {
            contactedList.get(entry).add(nodeInfo);
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
        getKBuckets().addNodeInfo(nodeInfo);
    }

    /**
     * The method is responsible for adding the node that responded to us,
     * to the k buckets list
     *
     * @param nodeInfo The node that answered us
     */
    public void gotResponse(NodeInfo nodeInfo) {
        getKBuckets().addNodeInfo(nodeInfo);
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
     * Do a sync ping to a node. If ping fails we will try to remove the
     * connection from our database
     *
     * @param nodeInfo The respective node info
     * @return True if ping successful, otherwise false
     */
    boolean doPingSync(NodeInfo nodeInfo) {
        NodeClient nodeClient = getNodeClient(nodeInfo);

        if (nodeClient == null) {
            return connectToNode(nodeInfo);
        } else {
            boolean success = nodeClient.doPingSync();

            if (!success) {
                nodeClients.remove(nodeClient);
            }

            return success;
        }
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
}
