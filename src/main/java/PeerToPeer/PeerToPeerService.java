package PeerToPeer;

import BlockChain.HashAlgorithm;
import grpcCode.PeerToPeerGrpc;
import grpcCode.PeerToPeerOuterClass;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// TODO: Check if we want the server to open a connection to the other node

public class PeerToPeerService extends PeerToPeerGrpc.PeerToPeerImplBase {
    public static Logger LOGGER = LogManager.getLogger(PeerToPeerService.class);

    @Override
    public void ping(PeerToPeerOuterClass.PingInfo request, StreamObserver<PeerToPeerOuterClass.PingSuccess> responseObserver) {
        PeerToPeerOuterClass.NodeInfo nodeDest = request.getDest();
        PeerToPeerOuterClass.NodeInfo nodeOrig = request.getOrig();

        NodeInfo nodeInfoOrig = new NodeInfo(nodeOrig);
        LOGGER.info("Got ping: From: " + HashAlgorithm.byteToHex(nodeInfoOrig.getId()));

        PeerToPeerOuterClass.PingSuccess.Builder response =
                PeerToPeerOuterClass.PingSuccess.newBuilder();

        response.setSuccess(isPingValid(nodeDest));
        LOGGER.info("Sending Ping response: Response: " + response.getSuccess());

        if (response.getSuccess()) {
            // Server that received the ping also adds the node to the known node
            // list, by opening a connection
            PeerToPeer.getRunningNode().connectToNode(convertToNodeInfo(nodeOrig));
        } else {
            LOGGER.info("Wrong dest: SupposedNodeId0: " +
                    PeerToPeer.getRunningNode().getNodeInfo().getIdString() +
                    ": GotNodeId1: " + convertToNodeInfo(nodeDest).getIdString());
        }

        responseObserver.onNext(response.build());

        responseObserver.onCompleted();
    }

    /**
     * Given a grpc {@link grpcCode.PeerToPeerOuterClass.NodeInfo}, defined
     * in the proto file, this method converts it to a standard
     * {@link NodeInfo} object
     *
     * @param nodeInfo the info to convert
     * @return A {@link NodeInfo} object
     */
    public NodeInfo convertToNodeInfo(PeerToPeerOuterClass.NodeInfo nodeInfo) {
        byte[] id = nodeInfo.getNodeId().toByteArray();
        String ip = nodeInfo.getNodeIp();
        int port = nodeInfo.getNodePort();

        return new NodeInfo(id, ip, port);
    }

    /**
     * Checks if a ping request is valid. It does so, by checking if the ping
     * was correctly sent for him, i.e. checks the NodeInfo sent by the sender.
     * If ping is valid
     *
     * @param nodeInfoDest info of the destination node
     * @return true if ping is valid, otherwise false
     */
    public boolean isPingValid(PeerToPeerOuterClass.NodeInfo nodeInfoDest) {
        return PeerToPeer.getRunningNode()
                .sameNodeInfo(convertToNodeInfo(nodeInfoDest));
    }
}
