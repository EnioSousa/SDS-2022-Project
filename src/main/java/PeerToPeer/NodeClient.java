package PeerToPeer;

import grpcCode.PeerToPeerGrpc;
import grpcCode.PeerToPeerOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TODO: Change plain text mode to a secure one
 * TODO: Write locks
 */

public class NodeClient {
    /**
     * Logger
     */
    public static Logger LOGGER = LogManager.getLogger(NodeClient.class);
    /**
     * Channel connection
     */
    private ManagedChannel channel;
    /**
     * Async stub
     */
    private PeerToPeerGrpc.PeerToPeerStub stub;
    /**
     * Node that's running this client
     */
    private final Node node;
    /**
     * Info of the node we are connected
     */
    private final NodeInfo connectedNodeInfo;

    public NodeClient(Node ourNode, NodeInfo otherNodeInfo) {
        node = ourNode;
        connectedNodeInfo = otherNodeInfo;

        openChannel(otherNodeInfo.getIp(), otherNodeInfo.getPort());
    }

    private void openChannel(String addressTo, int portTo) {
        this.channel = ManagedChannelBuilder.forAddress(addressTo, portTo)
                .usePlaintext()
                .build();

        this.stub = PeerToPeerGrpc.newStub(channel);
    }

    /**
     * This method is async and does a ping to a node.
     *
     * @param orig Sender node info
     * @param dest Destination node info
     */
    void doPing(NodeInfo orig, NodeInfo dest) {
        PeerToPeerOuterClass.NodeInfo nodeInfoOrig = orig.getServiceNodeInfo();

        PeerToPeerOuterClass.NodeInfo nodeInfoDest = dest.getServiceNodeInfo();

        PeerToPeerOuterClass.PingInfo request =
                PeerToPeerOuterClass.PingInfo.newBuilder()
                        .setOrig(nodeInfoOrig)
                        .setDest(nodeInfoDest)
                        .build();

        LOGGER.info("Do ping request: id: " + dest.getIdString());

        stub.ping(request, new StreamObserver<>() {
            @Override
            public void onNext(PeerToPeerOuterClass.PingSuccess value) {
                LOGGER.info("Response: " + value.getSuccess());

                // If ping successful adds the nodeClient to our list, otherwise
                // removes from the list
                if (value.getSuccess()) {
                    connectNodeClient();
                } else {
                    disconnectNodeClient();
                }
            }

            @Override
            public void onError(Throwable t) {
                LOGGER.error("CallBack error: " + t.getMessage());
                disconnectNodeClient();
            }

            @Override
            public void onCompleted() {
                LOGGER.info("Stream Completed: ");
            }
        });
    }

    private void connectNodeClient() {
        getNode().connectToNode(this);
    }

    private void disconnectNodeClient() {
        channel.shutdownNow();
        LOGGER.info("Closing channel from node: id: " + getConnectedNodeInfo().getIdString());

        getNode().disconnectFromNode(getConnectedNodeInfo());
    }

    public Node getNode() {
        return node;
    }

    public NodeInfo getConnectedNodeInfo() {
        return connectedNodeInfo;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;

        if (other == null || !(other instanceof NodeClient))
            return false;

        return getConnectedNodeInfo().equals(((NodeClient) other).connectedNodeInfo);
    }
}
