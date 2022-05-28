package PeerToPeer;

import BlockChain.HashAlgorithm;
import BlockChain.Transaction;
import com.google.common.primitives.Longs;
import com.google.protobuf.ByteString;
import grpcCode.PeerToPeerGrpc;
import grpcCode.PeerToPeerOuterClass.*;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import Utils.*;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.Random;

import java.util.LinkedList;

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


    @Override
    public void firstConn(InitMSG request, StreamObserver<InitResponse> responseObserver){
        String ip = request.getIp();
        long timeStamp = Instant.now().toEpochMilli();

        getRunningNode().addIpTimeStamp(ip,timeStamp);

        responseObserver.onNext(convertToInitResponse(timeStamp));
        responseObserver.onCompleted();
    }

    @Override
    public void getID(GetIdMSG request, StreamObserver<GetIdResponse> responseObserver){

        if (getRunningNode().verifyIpTime(request.getIp(), request.getTimeStamp())){
            LOGGER.info("Check if client has made the initial connection");


            byte[] challenge = request.getChallenge().toByteArray();

            LOGGER.info("Check if client has made the work");

            if(HashAlgorithm.validHash(challenge,InfoJoin.DIFFICULTY)){
                long time = request.getTimeStamp();
                int nonce = request.getNonce();

                byte[] work = null;

                try{
                    work = HashAlgorithm.generateHash(Longs.toByteArray(time), HashAlgorithm.intToByte(nonce));
                }catch (NoSuchAlgorithmException e){
                    LOGGER.info("Didnt do the initial computation");
                }

                if(Arrays.equals(challenge,work)){
                    /*
                     * Generate random id for the node
                     */

                    int randomId = generateRandomDigits(NodeInfo.SIZE_OF_ID);

                    byte[] nodeId = new byte[]{(byte)randomId};

                    LOGGER.info("Sending the new ID to the node");
                    responseObserver.onNext(convertToGetIdResponse(nodeId));
                    getRunningNode().addToUsedIds(nodeId);

                }

            }else{
                LOGGER.info("The sender didnt do the work");
            }
        }
        else{
            LOGGER.info("The sender is not in the list");
        }
        responseObserver.onCompleted();
    }


    public static TransactionMSG convertToTransactionMSG(byte[] sourceEnt, byte[] destEnt, byte[] productId, int bits){
        return TransactionMSG.newBuilder()
                .setSourceEnt(ByteString.copyFrom(sourceEnt))
                .setDestEnt(ByteString.copyFrom(destEnt))
                .setProductId(ByteString.copyFrom(productId))
                .setBits(bits)
                .build();
    }

    public static InitMSG convertToInitMSG(String nodeIp){
        return InitMSG.newBuilder()
                .setIp(nodeIp)
                .build();
    }

    public static InitResponse convertToInitResponse(long timeStamp){
        return InitResponse.newBuilder()
                .setTimeStamp(timeStamp)
                .build();
    }

    public static GetIdMSG convertToGetIdMSG(String ip, byte[] challenge, long timeStamp, int nonce){
        return GetIdMSG.newBuilder()
                .setIp(ip)
                .setChallenge(ByteString.copyFrom(challenge))
                .setTimeStamp(timeStamp)
                .setNonce(nonce)
                .build();
    }

    public static GetIdResponse convertToGetIdResponse(byte[] id){
        return GetIdResponse.newBuilder()
                .setId(ByteString.copyFrom(id))
                .build();
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
    // Generates a random int with n digits
    public static int generateRandomDigits(int n) {
        int m = (int) Math.pow(10, n - 1);
        return m + new Random().nextInt(9 * m);
    }
}
