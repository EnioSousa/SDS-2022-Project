package PeerToPeer;

import BlockChain.HashAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    public Node(NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
        LOGGER.info("New Node: Id:" + HashAlgorithm.byteToHex(nodeInfo.getId()));

        nodeServer = new NodeServer(this, nodeInfo.getPort());
        nodeServer.start();

        nodeClients = new ConcurrentLinkedQueue<>();
    }

    /**
     * Given the info of a node, checks the connection list for a node
     * connection, NodeClient, with the given information passed in the
     * parameters
     *
     * @param nodeInfo The info to search in the connection list
     * @return a nodeClient i.e. a connection to a node if it exists
     */
    public NodeClient getNodeClient(NodeInfo nodeInfo) {
        for (NodeClient nodeClient : nodeClients) {
            if (nodeClient.getConnectedNodeInfo().equals(nodeInfo))
                return nodeClient;
        }

        return null;
    }

    /**
     * Given a node info, open a new channel connection
     *
     * @param nodeInfo node info
     */
    public void connectToNode(NodeInfo nodeInfo) {
        connectToNode(new NodeClient(this, nodeInfo));
    }

    /**
     * Connect to node, i.e. save the node connection (NodeClient) in a
     * list
     *
     * @param nodeClient node client to save
     */
    public void connectToNode(NodeClient nodeClient) {
        if (!nodeClients.contains(nodeClient)) {
            nodeClients.add(nodeClient);
            LOGGER.info("New node connection: id: " +
                    nodeClient.getConnectedNodeInfo().getIdString());
        } else {
            LOGGER.info("Node already connected: id: " +
                    nodeClient.getConnectedNodeInfo().getIdString());
        }
    }

    /**
     * Disconnects from a node that has the info passed in the parameters
     *
     * @param nodeInfo Info of the node we want to remove
     */
    public void disconnectFromNode(NodeInfo nodeInfo) {
        NodeClient nodeClient = getNodeClient(nodeInfo);

        if (nodeClient == null) {
            LOGGER.info("Node doesn't exist: id: " + nodeInfo.getIdString());
        } else {
            disconnectFromNode(nodeClient);
        }
    }

    /**
     * Disconnect from a node client
     *
     * @param nodeClient node client we want to disconnect
     */
    public void disconnectFromNode(NodeClient nodeClient) {
        if (nodeClients.remove(nodeClient)) {
            LOGGER.info("Removed node from connected list: id: " +
                    nodeClient.getConnectedNodeInfo().getIdString());
        } else {
            LOGGER.info("Failed to removed node: id: " +
                    nodeClient.getConnectedNodeInfo().getIdString());
        }
    }

    /**
     * Do ping rpc call. If ping is successful, we will add the node into a
     * known connection list, i.e. open connections
     *
     * @param nodeInfoDest Node info to do the rpc call
     */
    public void doPing(NodeInfo nodeInfoDest) {
        NodeClient nodeClient = getNodeClient(nodeInfoDest);

        if (nodeClient == null) {
            nodeClient = new NodeClient(this, nodeInfoDest);
        }

        nodeClient.doPing(this.nodeInfo, nodeInfoDest);
    }

    /**
     * Checks if the Node info passed in the arguments is the same info on
     * this particular node
     *
     * @param nodeInfo info we want to check
     * @return true if the info are the same, toherwise false
     */
    public boolean sameNodeInfo(NodeInfo nodeInfo) {
        return nodeInfo.equals(getNodeInfo());
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

}
