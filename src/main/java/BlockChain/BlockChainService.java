package BlockChain;

import PeerToPeer.Node;
import PeerToPeer.NodeInfo;
import grpcCode.BlockChainGrpc;
import grpcCode.BlockChainOuterClass.NodeInfoMSG;
import grpcCode.BlockChainOuterClass.Success;
import grpcCode.BlockChainOuterClass.TransactionMSG;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    public void sendTransaction(TransactionMSG request, StreamObserver<Success> responseObserver) {
        Transaction transaction = convertToTransaction(request);
        NodeInfo nodeInfo = convertToNodeInfo(request.getNodeInfo());

        if (Security.verifyData(request.getSignature().toByteArray())) {
            LOGGER.info("Got transaction from: " + nodeInfo +
                    ": Transaction: " + transaction);

            getRunningNode().gotRequest(nodeInfo);

            if (Security.verifyTransaction(transaction)) {
                LOGGER.info("Transaction: " + transaction + ": valid:");
                // TODO: Do something else
            } else {
                LOGGER.info("Transaction: " + transaction + ": invalid:");
            }
        } else {
            LOGGER.info("Signature did not match: from: " + nodeInfo);
        }
    }

    public static Transaction convertToTransaction(TransactionMSG request) {
        byte[] source =
                Security.decrypt(request.getSourceEntity().toByteArray());

        byte[] dest =
                Security.decrypt(request.getDestEntity().toByteArray());

        byte[] productId =
                Security.decrypt(request.getProductId().toByteArray());

        byte[] bidTrans =
                Security.decrypt(request.getBidTrans().toByteArray());

        return new Transaction(source, dest, productId
                , HashAlgorithm.byteToInt(bidTrans));
    }

    public static NodeInfo convertToNodeInfo(NodeInfoMSG nodeInfoMSG) {
        byte[] id = Security.decrypt(nodeInfoMSG.getNodeId().toByteArray());
        byte[] ip = Security.decrypt(nodeInfoMSG.getNodeIp().toByteArray());
        byte[] port = Security.decrypt(nodeInfoMSG.getNodePort().toByteArray());

        return new NodeInfo(id, new String(ip), HashAlgorithm.byteToInt(port));
    }
}
