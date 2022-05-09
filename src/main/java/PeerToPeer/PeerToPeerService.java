package PeerToPeer;

import BlockChain.HashAlgorithm;
import com.google.protobuf.ByteString;
import grpcCode.PeerToPeerGrpc;
import grpcCode.PeerToPeerOuterClass.FindNodeMSG;
import grpcCode.PeerToPeerOuterClass.NodeInfoMSG;
import grpcCode.PeerToPeerOuterClass.SaveMSG;
import grpcCode.PeerToPeerOuterClass.SuccessMSG;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;

// TODO: Check if we want the server to open a connection to the other node

public class PeerToPeerService extends PeerToPeerGrpc.PeerToPeerImplBase {
    public static Logger LOGGER =
            LogManager.getLogger(PeerToPeerService.class);

    public static Node runningNode;

    public static void setRunningNode(Node node) {
        runningNode = node;
    }

    public static Node getRunningNode() {
        return runningNode;
    }

    @Override
    public void store(SaveMSG request, StreamObserver<SuccessMSG> responseObserver) {
        NodeInfo nodeInfo =
                new NodeInfo(request.getRequester().getNodeId().toByteArray(),
                        request.getRequester().getNodeIp(),
                        request.getRequester().getNodePort());

        LOGGER.info("Got store request: From: " + nodeInfo);

        boolean success =
                getRunningNode().storeValue(request.getKey().toByteArray(),
                        request.getValue().toByteArray());

        responseObserver.onNext(convertToSuccessMsg(success));

        responseObserver.onCompleted();

        // Try to add the requester info into our k bucket list
        getRunningNode().gotRequest(nodeInfo);
    }

    @Override
    public void findNode(FindNodeMSG request,
                         StreamObserver<NodeInfoMSG> responseObserver) {
        NodeInfo nodeInfo =
                new NodeInfo(request.getRequester().getNodeId().toByteArray(),
                        request.getRequester().getNodeIp(),
                        request.getRequester().getNodePort());

        LOGGER.info("Got findNode request: From: " + nodeInfo + ": id: " +
                HashAlgorithm.byteToHex(request.getWantedId().toByteArray()));

        LinkedList<NodeInfo> list =
                getRunningNode().getKBuckets().getKClosest(request.getWantedId().toByteArray());

        if (list.size() != 0) {
            for (int i = 0; i < runningNode.getKBuckets().getK() && i < list.size(); i++) {
                LOGGER.info("Sending to: " + nodeInfo + ": node: " + list.get(i));
                responseObserver.onNext(convertToNodeInfoMSG(list.get(i)));
            }
        }

        responseObserver.onCompleted();

        // Try to add the requester info into our k bucket list
        getRunningNode().gotRequest(nodeInfo);
    }

    @Override
    public void ping(NodeInfoMSG request,
                     StreamObserver<SuccessMSG> responseObserver) {

        NodeInfo nodeInfo = new NodeInfo(request.getNodeId().toByteArray(),
                request.getNodeIp(), request.getNodePort());

        LOGGER.info("Got ping request: From: " + nodeInfo);

        LOGGER.info("Sending ping response: True: To: " + nodeInfo);
        responseObserver.onNext(convertToSuccessMsg(true));

        responseObserver.onCompleted();

        // Try to add the requester info into our k bucket list
        getRunningNode().gotRequest(nodeInfo);
    }

    public static SuccessMSG convertToSuccessMsg(boolean bool) {
        return SuccessMSG.newBuilder()
                .setSuccess(bool)
                .build();
    }

    public static SaveMSG convertToSaveMSG(NodeInfo nodeInfo, byte[] key,
                                           byte[] value) {
        return SaveMSG.newBuilder()
                .setRequester(convertToNodeInfoMSG(nodeInfo))
                .setKey(ByteString.copyFrom(key))
                .setValue(ByteString.copyFrom(value))
                .build();
    }

    public static NodeInfoMSG convertToNodeInfoMSG(NodeInfo nodeInfo) {
        return NodeInfoMSG.newBuilder()
                .setNodeId(ByteString.copyFrom(nodeInfo.getId()))
                .setNodeIp(nodeInfo.getIp())
                .setNodePort(nodeInfo.getPort())
                .build();
    }

    public static NodeInfo convertToNodeInfo(NodeInfoMSG nodeInfoMSG) {
        return new NodeInfo(nodeInfoMSG.getNodeId().toByteArray(),
                nodeInfoMSG.getNodeIp(), nodeInfoMSG.getNodePort());
    }

    public static FindNodeMSG convertToFindNodeMSG(NodeInfo nodeInfo,
                                                   byte[] wantedId) {
        return FindNodeMSG.newBuilder()
                .setRequester(convertToNodeInfoMSG(nodeInfo))
                .setWantedId(ByteString.copyFrom(wantedId))
                .build();
    }
}
