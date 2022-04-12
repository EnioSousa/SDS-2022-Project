package HelloWorld;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class HelloWorldServer {
    public static final Logger LOGGER = LogManager.getLogger(HelloWorldServer.class);

    public static void main(String args[]) {
        Server server = ServerBuilder.forPort(5000)
                .addService(new HelloWorldService())
                .build();

        try {
            server.start();
            LOGGER.info("Server init: Port: " + server.getPort());
        } catch (IOException e) {
            LOGGER.error("Server init error: " + e);
        }

        // Await for server termination
        try {
            server.awaitTermination();
        } catch (InterruptedException e) {
            LOGGER.error("Server error: " + e);
        }
    }
}
