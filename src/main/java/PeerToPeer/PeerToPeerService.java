package PeerToPeer;

import BlockChain.Block;
import BlockChain.BlockHeader;
import BlockChain.HashAlgorithm;
import BlockChain.Transaction;
import Utils.BlockChain;
import Utils.InfoJoin;
import com.google.common.primitives.Longs;
import com.google.protobuf.ByteString;
import grpcCode.PeerToPeerGrpc;
import grpcCode.PeerToPeerOuterClass.*;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Arrays;
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
    public void firstConn(InitMSG request, StreamObserver<InitResponse> responseObserver) {

        String ip = request.getIp();

        LOGGER.info("Got request for timestamp: From: IP: " + request.getIp());

        long timeStamp = Instant.now().toEpochMilli();

        getRunningNode().addIpTimeStamp(ip, timeStamp);

        LOGGER.info("Sending timestamp: To: IP: " + request.getIp() +
                ": TimeStamp: " + timeStamp);
        responseObserver.onNext(convertToInitResponse(timeStamp));
        responseObserver.onCompleted();
    }

    @Override
    public void getID(GetIdMSG request, StreamObserver<GetIdResponse> responseObserver) {

        LOGGER.info("Verify if client: IP: " + request.getIp() + " is on the list of contacted IPs");

        if (getRunningNode().verifyIpTime(request.getIp(), request.getTimeStamp())) {

            LOGGER.info("Client with IP: " + request.getIp() + " passed initial connection check");

            byte[] challenge = request.getChallenge().toByteArray();

            LOGGER.info("Check if client with IP: " + request.getIp() + " Timestamp: " + request.getTimeStamp()
                    + " Nonce: " + request.getNonce() + " made the work");

            if (HashAlgorithm.validHash(challenge, InfoJoin.DIFFICULTY)) {
                long time = request.getTimeStamp();
                int nonce = request.getNonce();

                byte[] work = null;

                try {
                    work = HashAlgorithm.generateHash(Longs.toByteArray(time), HashAlgorithm.intToByte(nonce));
                } catch (NoSuchAlgorithmException e) {
                    LOGGER.info("Error generating the hash");
                }

                if (Arrays.equals(challenge, work)) {
                    LOGGER.info("Hash from IP: " + request.getIp() + " Challenge: " + HashAlgorithm.byteToHex(request.getChallenge().toByteArray())
                            + " matches Hash from Bootstrap: " + HashAlgorithm.byteToHex(work));

                    byte[] nodeId =
                            HashAlgorithm.generateRandomByteArray(Node.getIdSize());

                    // TODO: Verify that id doesn't exist

                    LOGGER.info("Sending the new ID: " + HashAlgorithm.byteToHex(nodeId) + " to the node: IP: " + request.getIp());

                    responseObserver.onNext(convertToGetIdResponse(nodeId));
                    getRunningNode().addToUsedIds(nodeId);
                }

            } else {
                // TODO: Make better log
                LOGGER.info("The sender didnt do the work: Expected: " +
                        HashAlgorithm.byteToHex(challenge));
            }
        } else {
            LOGGER.info("The sender is not in the list: IP: " + request.getIp());
        }

        responseObserver.onCompleted();
    }


    @Override
    public void sendBlockChainSize(NodeInfoMSG request,
                                   StreamObserver<BlockChainSizeMSG> responseObserver) {
        NodeInfo nodeInfo = convertToNodeInfo(request);

        getRunningNode().gotRequest(nodeInfo);

        LOGGER.info("Got request from: " + nodeInfo);

        responseObserver.onNext(BlockChainSizeMSG.newBuilder().setSize(
                getRunningNode().getBlockChain().getBlockChain().size()).build());

        responseObserver.onCompleted();
    }

    @Override
    public void sendFullBlockChain(NodeInfoMSG request, StreamObserver<BlockContentMSG> responseObserver) {
        NodeInfo nodeInfo = convertToNodeInfo(request);

        LOGGER.info("Got request for full block chain from: " + nodeInfo);

        LinkedList<Block> blockChain = getRunningNode().getBlockChain().getBlockChain();

        for (Block block : blockChain) {
            responseObserver.onNext(convertToBlockContentMSG(block));
        }

        responseObserver.onCompleted();
    }

    @Override
    public void sendBlock(BlockMSG request,
                          StreamObserver<SuccessMSG> responseObserver) {
        NodeInfo nodeInfo = convertToNodeInfo(request.getNodeInfo());

        getRunningNode().gotRequest(nodeInfo);

        BlockHeader blockHeader =
                convertToBlockHeader(request.getBlockHeader());

        LinkedList<Transaction> list = new LinkedList<>();


        for (int i = 0; i < request.getTransactionCount(); i++) {
            list.add(convertToTransaction(request.getTransaction(i)));
        }

        try {
            Block block = new Block(blockHeader, list);

            LOGGER.info("Got block from: " + nodeInfo + ": Block: " + block);

            getRunningNode().addBlock(block);
        } catch (Exception e) {
            LOGGER.error("Failed to get block from: " + nodeInfo + ": Reason:" +
                    e);
        }
    }

    @Override
    public void sendTransaction(TransactionMSG request,
                                StreamObserver<SuccessMSG> responseObserver) {
        Transaction transaction = convertToTransaction(request);
        NodeInfo nodeInfo = convertToNodeInfo(request.getNodeInfo());

        // TODO: Verify transaction and signature
        if (true) {
            LOGGER.info("Got transaction from: " + nodeInfo +
                    ":Transaction :" + transaction);

            getRunningNode().gotRequest(nodeInfo);

            getRunningNode().addTransactionToPool(transaction);

            responseObserver.onNext(convertToSuccessMSG(true));
        } else {
            responseObserver.onNext(convertToSuccessMSG(false));
        }

        responseObserver.onCompleted();

        if (getRunningNode().getTransactionPool().size() >= BlockChain.blockChainMinTransaction) {
            getRunningNode().newBlock();
        }
    }

    public static InitMSG convertToInitMSG(String nodeIp) {
        return InitMSG.newBuilder()
                .setIp(nodeIp)
                .build();
    }

    public static InitResponse convertToInitResponse(long timeStamp) {
        return InitResponse.newBuilder()
                .setTimeStamp(timeStamp)
                .build();
    }

    public static GetIdMSG convertToGetIdMSG(String ip, byte[] challenge, long timeStamp, int nonce) {
        return GetIdMSG.newBuilder()
                .setIp(ip)
                .setChallenge(ByteString.copyFrom(challenge))
                .setTimeStamp(timeStamp)
                .setNonce(nonce)
                .build();
    }

    public static GetIdResponse convertToGetIdResponse(byte[] id) {
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

    public static Block convertToBlock(BlockContentMSG cnt) throws NoSuchAlgorithmException {
        LinkedList<Transaction> list = new LinkedList<>();

        for (TransactionContentMSG tran : cnt.getTransactionList()) {
            list.add(convertToTransaction(tran));
        }

        return new Block(convertToBlockHeader(cnt.getBlockHeader()), list);
    }

    public static BlockContentMSG convertToBlockContentMSG(Block block) {
        BlockContentMSG.Builder builder = BlockContentMSG.newBuilder()
                .setBlockHeader(convertToBlockHeaderMSG(block.getBlockHeader()));

        for (int i = 0; i < block.getTransactionsList().size(); i++) {
            builder.setTransaction(i,
                    convertToTransactionContentMSG(block.getTransactionsList().get(i)));
        }

        return builder.build();
    }

    public static BlockHeaderMSG convertToBlockHeaderMSG(BlockHeader blockHeader) {
        return BlockHeaderMSG.newBuilder()
                .setDifficulty(blockHeader.getDifficulty())
                .setMerkleRoot(ByteString.copyFrom(blockHeader.getMerkleTreeHash()))
                .setNonce(blockHeader.getNonce())
                .setPrevHash(ByteString.copyFrom(blockHeader.getPrevHash()))
                .setVersion(blockHeader.getVersion())
                .setTime(blockHeader.getUnixTimestamp())
                .build();
    }

    public static Transaction convertToTransaction(TransactionMSG request) {
        return new Transaction(request.getSourceEntity().toByteArray(),
                request.getDestEntity().toByteArray(),
                request.getProductId().toByteArray(),
                request.getBidTrans());
    }

    public static TransactionContentMSG convertToTransactionContentMSG(Transaction transaction) {
        return TransactionContentMSG.newBuilder()
                .setBidTrans(transaction.getBidTrans())
                .setDestEntity(ByteString.copyFrom(transaction.getDestEntity()))
                .setSourceEntity(ByteString.copyFrom(transaction.getSourceEntity()))
                .setProductId(ByteString.copyFrom(transaction.getProductId()))
                .build();
    }

    public static Transaction convertToTransaction(TransactionContentMSG request) {
        return new Transaction(request.getSourceEntity().toByteArray(),
                request.getDestEntity().toByteArray(),
                request.getProductId().toByteArray(),
                request.getBidTrans());
    }

    public static BlockHeader convertToBlockHeader(BlockHeaderMSG request) {
        return new BlockHeader(request.getVersion(), request.getTime(),
                request.getDifficulty(), request.getPrevHash().toByteArray(),
                request.getMerkleRoot().toByteArray(), request.getNonce());
    }

    public static SuccessMSG convertToSuccessMSG(boolean bool) {
        return SuccessMSG.newBuilder().setSuccess(bool).build();
    }

    public static TransactionMSG convertToTransactionMSG(Transaction transaction) {
        return TransactionMSG.newBuilder()
                .setBidTrans(transaction.getBidTrans())
                .setDestEntity(ByteString.copyFrom(transaction.getDestEntity()))
                .setSourceEntity(ByteString.copyFrom(transaction.getSourceEntity()))
                .setProductId(ByteString.copyFrom(transaction.getProductId()))
                .setNodeInfo(convertToNodeInfoMSG(getRunningNode().getNodeInfo()))
                .build();
    }

    public static BlockMSG convertToBlockMSG(Block block) {
        BlockMSG.Builder builder = BlockMSG.newBuilder()
                .setBlockHeader(convertToBlockHeaderMSG(block.getBlockHeader()))
                .setNodeInfo(convertToNodeInfoMSG(getRunningNode().getNodeInfo()));

        for (int i = 0; i < block.getTransactionsList().size(); i++) {
            builder.setTransaction(i,
                    convertToTransactionContentMSG(block.getTransactionsList().get(i)));
        }

        return builder.build();
    }
}
