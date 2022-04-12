package PeerToPeer;

import BlockChain.HashAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;

public class Node {
    /**
     * Logger object to log stuff
     */
    public static final Logger LOGGER = LogManager.getLogger(Node.class);
    /**
     * Info of the current node
     */
    private NodeInfo nodeInfo;
    /**
     * List of connected nodes
     */
    private LinkedList<NodeInfo> nodeList;
    /**
     * Node server
     */
    private NodeServer nodeServer;

    private NodeClient nodeClient;

    public Node(NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
        LOGGER.info("New Node: Id:" + HashAlgorithm.byteToHex(nodeInfo.getId()));

        this.nodeList = new LinkedList<>();

        nodeServer = new NodeServer(nodeInfo.getPort());
    }
}
