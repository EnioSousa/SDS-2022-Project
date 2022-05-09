package PeerToPeer;

import BlockChain.HashAlgorithm;
import grpcCode.PeerToPeerGrpc;
import grpcCode.PeerToPeerOuterClass.FindNodeMSG;
import grpcCode.PeerToPeerOuterClass.NodeInfoMSG;
import grpcCode.PeerToPeerOuterClass.SaveMSG;
import grpcCode.PeerToPeerOuterClass.SuccessMSG;
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
     * sync stub
     */
    private PeerToPeerGrpc.PeerToPeerBlockingStub syncStub;
    /**
     * Async stub
     */
    private PeerToPeerGrpc.PeerToPeerStub asyncStub;
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

    /**
     * Creates the grpc channel and respective stubs
     *
     * @param addressTo The address to connect to
     * @param portTo    the port to connect to
     */
    private void openChannel(String addressTo, int portTo) {
        this.channel = ManagedChannelBuilder.forAddress(addressTo, portTo)
                .usePlaintext()
                .build();

        this.asyncStub = PeerToPeerGrpc.newStub(channel);

        this.syncStub = PeerToPeerGrpc.newBlockingStub(channel);
    }

    void doStore(byte[] key, byte[] value) {
        LOGGER.info("Doing store to: " + connectedNodeInfo + ": key: "
                + HashAlgorithm.byteToHex(key) + ": value: "
                + HashAlgorithm.byteToHex(value));

        SaveMSG saveMSG =
                PeerToPeerService.convertToSaveMSG(node.getNodeInfo(), key,
                        value);

        asyncStub.store(saveMSG, new StreamObserver<SuccessMSG>() {
            @Override
            public void onNext(SuccessMSG val) {
                LOGGER.info("Got store response from: " + connectedNodeInfo +
                        ": key: "
                        + HashAlgorithm.byteToHex(key) + ": value: "
                        + HashAlgorithm.byteToHex(value) + ": success: "
                        + val.getSuccess());
            }

            @Override
            public void onError(Throwable t) {
                LOGGER.info("Error store to: " + connectedNodeInfo + ": key: "
                        + HashAlgorithm.byteToHex(key) + ": value: "
                        + HashAlgorithm.byteToHex(value));
            }

            @Override
            public void onCompleted() {
                getNode().gotResponse(getConnectedNodeInfo());
            }
        });

    }

    /**
     * Send a find node request. This method will also recursively do another
     * session of find nodes, depending on the response, i.e. if the nodes
     * from the response have been previously contacted or not. This
     * method is async, so we need a list of nodes we have contacted for this
     * request. The request group can be identified by the entry value
     *
     * @param wantedId The id we want to find
     * @param alpha    the alpha value, i.e. the maximum depth of calls
     * @param entry    the entry identifying
     */
    void doFindNode(byte[] wantedId, int alpha, byte[] entry) {
        if (alpha >= getNode().getKBuckets().getAlpha()) {
            return;
        }

        LOGGER.info("Doing find node: To: " + connectedNodeInfo);

        FindNodeMSG findNodeMSG =
                PeerToPeerService.convertToFindNodeMSG(node.getNodeInfo(),
                        wantedId);

        for (int i = 0; i < alpha; i++) {
            asyncStub.findNode(findNodeMSG,
                    new StreamObserver<NodeInfoMSG>() {
                        @Override
                        public void onNext(NodeInfoMSG value) {
                            NodeInfo nodeInfo =
                                    PeerToPeerService.convertToNodeInfo(value);

                            LOGGER.info("Got find node response: " + nodeInfo);

                            getNode().doFindNode(nodeInfo, wantedId, alpha + 1, entry);
                        }

                        @Override
                        public void onError(Throwable t) {
                            LOGGER.error("Find node error: " + connectedNodeInfo + ":" +
                                    " " + HashAlgorithm.byteToHex(wantedId));
                        }

                        @Override
                        public void onCompleted() {
                            LOGGER.info("End of find node: " + connectedNodeInfo +
                                    ": wantedId: " + HashAlgorithm.byteToHex(wantedId));

                            getNode().gotResponse(getConnectedNodeInfo());
                        }
                    });

        }
    }

    /**
     * Do an async ping call
     */
    void doPingAsync() {
        LOGGER.info("Doing ping request: To: " + connectedNodeInfo);

        NodeInfoMSG nodeInfoMsg =
                PeerToPeerService.convertToNodeInfoMSG(node.getNodeInfo());

        asyncStub.ping(nodeInfoMsg,
                new StreamObserver<SuccessMSG>() {
                    @Override
                    public void onNext(SuccessMSG value) {
                        LOGGER.info("Got Ping Response: from: " + connectedNodeInfo +
                                ": Value: " + value.getSuccess());
                    }

                    @Override
                    public void onError(Throwable t) {
                        LOGGER.error("Ping error: From: " + connectedNodeInfo);
                    }

                    @Override
                    public void onCompleted() {
                        LOGGER.info("Stream completed: From: " + connectedNodeInfo);

                        getNode().gotResponse(getConnectedNodeInfo());
                    }
                });
    }

    /**
     * Do  sync ping, i.e. wait for the ping rpc call to return
     *
     * @return True if ping was successful otherwise false
     */
    boolean doPingSync() {
        NodeInfoMSG nodeInfoMsg =
                PeerToPeerService.convertToNodeInfoMSG(node.getNodeInfo());

        LOGGER.info("Doing ping request: To: " + connectedNodeInfo);
        SuccessMSG pingSuccessMSG = syncStub.ping(nodeInfoMsg);

        LOGGER.info("Got Ping Response: from: " + connectedNodeInfo +
                ": Value: " + pingSuccessMSG.getSuccess());

        return pingSuccessMSG.getSuccess();
    }

    /**
     * Close this client grpc channel
     */
    public void closeClient() {
        LOGGER.info("Shutting down channel: " + getConnectedNodeInfo());
        channel.shutdownNow();
    }

    /**
     * Get node running this client
     *
     * @return the node running this client
     */
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

        if (other == null)
            return false;

        if (other instanceof NodeClient) {
            NodeClient nodeClient = (NodeClient) other;

            return getConnectedNodeInfo().equals(nodeClient.getConnectedNodeInfo());
        }

        if (other instanceof NodeInfo) {
            NodeInfo nodeInfo = (NodeInfo) other;

            return getConnectedNodeInfo().equals(nodeInfo);
        }

        return false;
    }
}
