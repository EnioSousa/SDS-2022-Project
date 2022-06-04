package BlockChain;

import PeerToPeer.Node;
import PeerToPeer.NodeInfo;
import grpcCode.BlockChainGrpc;
import grpcCode.BlockChainOuterClass.*;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;

public class BlockChainService extends BlockChainGrpc.BlockChainImplBase {
    public static Logger LOGGER =
            LogManager.getLogger(BlockChainService.class);

    public static Node runningNode;

    public static void setRunningNode(Node node) {
        runningNode = node;
    }

    public static Node getRunningNode() {
        return runningNode;
    }

    @Override
    public void sendBlock(BlockMSG request, StreamObserver<Success> responseObserver) {
        NodeInfo nodeInfo = convertToNodeInfo(request.getNodeInfo());

        BlockHeader blockHeader =
                convertToBlockHeader(request.getBlockHeader());

        LinkedList<Transaction> list = new LinkedList<>();


        for (int i = 0; i < request.getTransactionCount(); i++) {
            list.add(convertToTransaction(request.getTransaction(i)));
        }

        try {
            Block block = new Block(blockHeader, list);
        } catch (Exception e) {

        }
    }

    @Override
    public void sendTransaction(TransactionMSG request, StreamObserver<Success> responseObserver) {
        Transaction transaction = convertToTransaction(request);
        NodeInfo nodeInfo = convertToNodeInfo(request.getNodeInfo());

        // TODO: Verify transaction and signature
        if (true) {
            LOGGER.info("Got transaction from: " + nodeInfo +
                    ":Transaction :" + transaction);

            getRunningNode().gotRequest(nodeInfo);
        }
    }

    public static Transaction convertToTransaction(TransactionMSG request) {
        return new Transaction(request.getSourceEntity().toByteArray(),
                request.getDestEntity().toByteArray(),
                request.getProductId().toByteArray(),
                request.getBidTrans());
    }

    public static Transaction convertToTransaction(TransactionContentMSG request) {
        return new Transaction(request.getSourceEntity().toByteArray(),
                request.getDestEntity().toByteArray(),
                request.getProductId().toByteArray(),
                request.getBidTrans());
    }

    public static NodeInfo convertToNodeInfo(NodeInfoMSG request) {
        return new NodeInfo(request.getNodeId().toByteArray(),
                request.getNodeIp(), request.getNodePort());
    }

    public static BlockHeader convertToBlockHeader(BlockHeaderMSG request) {
        return new BlockHeader(request.getVersion(), request.getTime(),
                request.getDifficulty(), request.getPrevHash().toByteArray(),
                request.getMerkleRoot().toByteArray(), request.getNonce());
    }

}
