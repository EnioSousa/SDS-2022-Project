package PeerToPeer;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class NodeServer extends Thread {
    /**
     * Logger object
     */
    public static Logger LOGGER = LogManager.getLogger(NodeServer.class);
    /**
     * The grpc server
     */
    private final Server server;
    /**
     * Port associated with the server
     */
    private final int port;
    /**
     * The node running this server
     */
    private final Node node;

    /**
     * Given a port, this constructor creates a grpc server
     *
     * @param port port to associate the server
     */
    public NodeServer(Node node, int port) {
        this.port = port;
        this.node = node;

        this.server = ServerBuilder.forPort(this.port)
                .addService(new PeerToPeerService())
                .build();

        PeerToPeerService.setRunningNode(node);
    }

    @Override
    public void run() {
        LOGGER.info("Init Server Thread: ");

        try {
            server.start();
            LOGGER.info("Server init: Port: " + server.getPort());
        } catch (IOException e) {
            LOGGER.error("Server init error: " + e);
        }

        try {
            server.awaitTermination();
            LOGGER.info("Server termination: ");
        } catch (InterruptedException e) {
            LOGGER.error("Server interrupt error: " + e);
            stopServer();
        } finally {
            LOGGER.info("Close Server Thread: ");
        }
    }

    /**
     * Stop the server
     */
    public void stopServer() {
        if (!server.isShutdown()) {
            server.shutdown();
            LOGGER.info("Shutting down the server: ");
        }
    }

    /**
     * Get the port
     *
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * Return the node running this server
     *
     * @return The node running this server
     */
    public Node getNode() {
        return node;
    }
}
