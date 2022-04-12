package PeerToPeer;

import grpcCode.PeerToPeerGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TODO: Change plain text mode to a secure one
 */

public class NodeClient {
    public static Logger LOGGER = LogManager.getLogger(NodeClient.class);

    private final PeerToPeerGrpc.PeerToPeerBlockingStub stub;

    public NodeClient(String address, int port) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(address, port)
                .usePlaintext()
                .build();

        this.stub = PeerToPeerGrpc.newBlockingStub(channel);
    }

    void doPing(NodeInfo orig, NodeInfo dest) {
    }
}
