package HelloWorld;

import grpcCode.HelloWorldGrpc;
import grpcCode.Helloworld;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HelloWorldClient {
    public static final Logger LOGGER =
            LogManager.getLogger(HelloWorldClient.class);

    public static void main(String[] args) {
        // Use of plain text only for testing purposes
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost"
                , 5000).usePlaintext().build();

        // Blocking stubs, i.e. not async calls
        HelloWorldGrpc.HelloWorldBlockingStub stub =
                HelloWorldGrpc.newBlockingStub(channel);

        LOGGER.info("Sending hello request: Address: localhost: Port: 5000");
        Helloworld.HelloRequest request =
                Helloworld.HelloRequest.newBuilder().setMsg("Hello from Enio").build();

        Helloworld.HelloReply reply = stub.hello(request);
        LOGGER.info("Got reply: Message: " + reply.getMsg());
    }
}
