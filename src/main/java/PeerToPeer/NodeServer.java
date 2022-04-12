package PeerToPeer;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class NodeServer {
    public static Logger LOGGER = LogManager.getLogger(NodeServer.class);

    private final Server server;

    private final int port;

    public NodeServer(int port) {
        this.port = port;

        this.server = ServerBuilder.forPort(this.port)
                .addService(new PeerToPeerService())
                .build();

        try {
            server.start();
            LOGGER.info("Server init: ");
        } catch (IOException e) {
            LOGGER.error("Server init error: " + e);
        }

        try {
            server.awaitTermination();
            LOGGER.info("Server termination: ");
        } catch (InterruptedException e) {
            LOGGER.error("Server interrupt error: " + e);
        }
    }

    public Server getServer() {
        return server;
    }

    public int getPort() {
        return port;
    }
}
