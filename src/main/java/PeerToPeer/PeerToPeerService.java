package PeerToPeer;

import BlockChain.HashAlgorithm;
import com.google.protobuf.ByteString;
import grpcCode.PeerToPeerGrpc;
import grpcCode.PeerToPeerOuterClass.*;
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
    public void find(FindMSG request, StreamObserver<FindResponseMSG> responseObserver) {
        NodeInfo nodeInfo = convertToNodeInfo(request.getRequester());
        byte[] key = request.getKey().toByteArray();

        LOGGER.info("Got find request from: " + nodeInfo + ": key: "
                + HashAlgorithm.byteToHex(key));

        byte[] storedValue = getRunningNode().getStoredValue(key);

        if (storedValue == null) {
            LinkedList<NodeInfo> list =
                    getRunningNode().getKBuckets().getKClosest(key);

            LOGGER.info("Don't have the key: " + HashAlgorithm.byteToHex(key));

            for (NodeInfo info : list) {
                responseObserver.onNext(convertToFindNodeResponseMSG(info,
                        null));
            }
        } else {
            LOGGER.info("Have the key: " + HashAlgorithm.byteToHex(key));

            responseObserver.onNext(convertToFindNodeResponseMSG(null, storedValue));
        }

        responseObserver.onCompleted();
    }

    @Override
    public void store(SaveMSG request, StreamObserver<SuccessMSG> responseObserver) {
        NodeInfo nodeInfo = convertToNodeInfo(request.getRequester());

        LOGGER.info("Got store request: From: " + nodeInfo + ": key: " +
                HashAlgorithm.byteToHex(request.getKey().toByteArray()) +
                ": value: " +
                HashAlgorithm.byteToHex(request.getValue().toByteArray()));

        boolean success =
                getRunningNode().storeValue(request.getKey().toByteArray(),
                        request.getValue().toByteArray());

        LOGGER.info("keyPair success: " + success + ": " +
                HashAlgorithm.byteToHex(request.getKey().toByteArray()) + ": " +
                HashAlgorithm.byteToHex(request.getValue().toByteArray()));

        responseObserver.onNext(convertToSuccessMsg(success));

        responseObserver.onCompleted();

        // Try to add the requester info into our k bucket list
        getRunningNode().gotRequest(nodeInfo);
    }

    @Override
    public void findNode(FindNodeMSG request,
                         StreamObserver<NodeInfoMSG> responseObserver) {
        NodeInfo nodeInfo = convertToNodeInfo(request.getRequester());

        LOGGER.info("Got findNode request: From: " + nodeInfo + ": id: " +
                HashAlgorithm.byteToHex(request.getWantedId().toByteArray()));

        // Try to add the requester info into our k bucket list
        getRunningNode().gotRequest(nodeInfo);

        LinkedList<NodeInfo> list =
                getRunningNode().getKBuckets().getKClosest(request.getWantedId().toByteArray());

        if (list.size() != 0) {
            for (int i = 0; i < runningNode.getKBuckets().getK() && i < list.size(); i++) {
                LOGGER.info("Sending to: " + nodeInfo + ": node: " + list.get(i));
                responseObserver.onNext(convertToNodeInfoMSG(list.get(i)));
            }
        }

        responseObserver.onCompleted();
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

    public static FindResponseMSG convertToFindNodeResponseMSG(NodeInfo nodeInfo, byte[] value) {
        FindResponseMSG.Builder builder = FindResponseMSG.newBuilder();

        if (nodeInfo != null) {
            builder.setResponder(convertToNodeInfoMSG(nodeInfo));
        }

        if (value != null) {
            builder.setValue(ByteString.copyFrom(value));
        }

        return builder.build();
    }

    public static FindMSG convertToFindMSG(NodeInfo nodeInfo, byte[] key) {
        return FindMSG.newBuilder()
                .setRequester(convertToNodeInfoMSG(getRunningNode().getNodeInfo()))
                .setKey(ByteString.copyFrom(key))
                .build();
    }
}
