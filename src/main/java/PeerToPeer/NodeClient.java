package PeerToPeer;

import BlockChain.HashAlgorithm;
import grpcCode.PeerToPeerGrpc;
import grpcCode.PeerToPeerOuterClass.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * TODO: Change plain text mode to a secure one
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

    void doFind(byte[] key, int alpha, byte[] entry) {
        LOGGER.info("Doing find to: " + connectedNodeInfo + ": key: "
                + HashAlgorithm.byteToHex(key));

        FindMSG findMSG =
                PeerToPeerService.convertToFindMSG(getConnectedNodeInfo(), key);

        try {
            asyncStub.find(findMSG, new StreamObserver<>() {
                @Override
                public void onNext(FindResponseMSG value) {
                    if (value.hasResponder()) {
                        NodeInfo nodeInfo =
                                PeerToPeerService.convertToNodeInfo(value.getResponder());

                        LOGGER.info("Got node from find request: " + nodeInfo);

                        getNode().doFind(nodeInfo, key, alpha + 1, entry);
                    } else {
                        LOGGER.info("Got value from key: " + HashAlgorithm.byteToHex(key));
                        getNode().storeValue(key,
                                value.getValue().toByteArray());
                    }
                }

                @Override
                public void onError(Throwable t) {

                }

                @Override
                public void onCompleted() {
                    getNode().gotResponse(getConnectedNodeInfo());
                }
            });
        } catch (Exception e) {
            LOGGER.error("Connection unavailable: " + connectedNodeInfo + ": " + e);
        }

    }

    void doStore(byte[] key, byte[] value) {
        LOGGER.info("Doing store to: " + connectedNodeInfo + ": key: "
                + HashAlgorithm.byteToHex(key) + ": value: "
                + HashAlgorithm.byteToHex(value));

        SaveMSG saveMSG =
                PeerToPeerService.convertToSaveMSG(node.getNodeInfo(), key,
                        value);

        try {
            asyncStub.store(saveMSG, new StreamObserver<>() {
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
        } catch (Exception e) {
            LOGGER.error("Unavailable connection: " + getConnectedNodeInfo());
        }

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

        try {
            asyncStub.findNode(findNodeMSG,
                    new StreamObserver<>() {
                        @Override
                        public void onNext(NodeInfoMSG value) {
                            NodeInfo nodeInfo =
                                    PeerToPeerService.convertToNodeInfo(value);

                            LOGGER.info("Got find node response: " + nodeInfo);

                            getNode().getKBuckets().addNodeInfo(nodeInfo);

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
        } catch (Exception e) {
            LOGGER.error("Find node: Unavailable connection: " + connectedNodeInfo);
        }
    }

    /**
     * This client will try to ping a node (the old one), to see if he's still
     * on, in case it isn't, then we will replace the old node with a new one
     *
     * @param newInfo The candidate to replace
     * @param oldInfo The candidate to be replaced
     */
    void kBucketFullDoPing(NodeInfo newInfo, NodeInfo oldInfo) {
        LOGGER.info("Doing ping request: To: " + connectedNodeInfo);

        NodeInfoMSG nodeInfoMsg =
                PeerToPeerService.convertToNodeInfoMSG(node.getNodeInfo());

        final long oldTimer = System.currentTimeMillis();

        try {
            asyncStub.ping(nodeInfoMsg,
                    new StreamObserver<>() {
                        @Override
                        public void onNext(SuccessMSG value) {
                            long curTimer = System.currentTimeMillis();

                            if (curTimer - oldTimer >= 1500) {
                                LOGGER.info("Got Late Ping Response: from: " + connectedNodeInfo +
                                        ": Value: " + value.getSuccess() + ":" +
                                        " " + "delay: " + (curTimer - oldTimer));
                            } else {
                                LOGGER.info("Got Ping Response: from: " + connectedNodeInfo +
                                        ": Value: " + value.getSuccess() + ":" +
                                        " delay: " + (curTimer - oldTimer));

                                getNode().getKBuckets().replace(newInfo, oldInfo);
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            LOGGER.error("Ping error: From: " + connectedNodeInfo);

                            getNode().getKBuckets().replace(newInfo, oldInfo);
                        }

                        @Override
                        public void onCompleted() {
                            LOGGER.info("Stream completed: From: " + connectedNodeInfo);

                            getNode().gotResponse(getConnectedNodeInfo());
                        }
                    });
        } catch (Exception e) {
            LOGGER.error("Connection unavailable: " + getConnectedNodeInfo()
                    + ": error: " + e);

            getNode().getKBuckets().replace(newInfo, oldInfo);
        }
    }

    /**
     * Do an async ping call
     */
    void doPingAsync() {
        LOGGER.info("Doing ping request: To: " + connectedNodeInfo);

        NodeInfoMSG nodeInfoMsg =
                PeerToPeerService.convertToNodeInfoMSG(node.getNodeInfo());

        try {
            asyncStub.ping(nodeInfoMsg,
                    new StreamObserver<>() {
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
        } catch (Exception e) {
            LOGGER.error("Connection unavailable: " + getConnectedNodeInfo()
                    + ": error: " + e);
        }
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

        try {
            SuccessMSG pingSuccessMSG = syncStub.ping(nodeInfoMsg);

            LOGGER.info("Got Ping Response: from: " + connectedNodeInfo +
                    ": Value: " + pingSuccessMSG.getSuccess());

            return pingSuccessMSG.getSuccess();
        } catch (Exception e) {
            LOGGER.error("Connection unavailable: " + getConnectedNodeInfo()
                    + ": error: " + e);

            return false;
        }
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

        if (other instanceof NodeClient nodeClient) {

            return getConnectedNodeInfo().equals(nodeClient.getConnectedNodeInfo());
        }

        if (other instanceof NodeInfo nodeInfo) {

            return getConnectedNodeInfo().equals(nodeInfo);
        }

        return false;
    }

    @Override
    public String toString() {
        return getConnectedNodeInfo().toString();
    }
}
