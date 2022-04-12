package HelloWorld;

import grpcCode.HelloWorldGrpc;
import grpcCode.Helloworld;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HelloWorldService extends HelloWorldGrpc.HelloWorldImplBase {
    public static final Logger LOGGER = LogManager.getLogger(HelloWorldService.class);

    @Override
    public void hello(Helloworld.HelloRequest request, StreamObserver<Helloworld.HelloReply> responseObserver) {
        LOGGER.info("new rpc call: hello");

        String msg = request.getMsg();
        LOGGER.info("received msg: " + msg);

        Helloworld.HelloReply.Builder response = Helloworld.HelloReply.newBuilder();
        response.setMsg("Hello");

        LOGGER.info("sending msg: " + response.getMsg());
        // Try to end message. Method is async
        responseObserver.onNext(response.build());

        // Close the connection
        responseObserver.onCompleted();
    }
}
