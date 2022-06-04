package BlockChain;

import PeerToPeer.Node;
import grpcCode.BlockChainGrpc;
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
        
    }
}
