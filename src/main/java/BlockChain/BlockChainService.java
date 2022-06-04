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

        if (getRunningNode().getSecurity().verifyData(request.getSignature().toByteArray())) {
            LOGGER.info("Got transaction from: " + nodeInfo +
                    ": Transaction: " + transaction);

            getRunningNode().gotRequest(nodeInfo);

            if (getRunningNode().getSecurity().verifyTransaction(transaction)) {
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
        try {
            byte[] source =
                    getRunningNode().getSecurity().decrypt(request.getSourceEntity().toByteArray());

            byte[] dest =
                    getRunningNode().getSecurity().decrypt(request.getDestEntity().toByteArray());

            byte[] productId =
                    getRunningNode().getSecurity().decrypt(request.getProductId().toByteArray());

            byte[] bidTrans =
                    getRunningNode().getSecurity().decrypt(request.getBidTrans().toByteArray());

            return new Transaction(source, dest, productId
                    , HashAlgorithm.byteToInt(bidTrans));
        } catch (Exception e) {
            return null;
        }
    }

    public static NodeInfo convertToNodeInfo(NodeInfoMSG nodeInfoMSG) {
        try {
            byte[] id = getRunningNode().getSecurity().decrypt(nodeInfoMSG.getNodeId().toByteArray());
            byte[] ip = getRunningNode().getSecurity().decrypt(nodeInfoMSG.getNodeIp().toByteArray());
            byte[] port = getRunningNode().getSecurity().decrypt(nodeInfoMSG.getNodePort().toByteArray());

            return new NodeInfo(id, new String(ip), HashAlgorithm.byteToInt(port));
        } catch (Exception e) {
            return null;
        }
    }
}
