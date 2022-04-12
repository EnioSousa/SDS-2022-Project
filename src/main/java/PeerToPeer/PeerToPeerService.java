package PeerToPeer;

import grpcCode.PeerToPeerGrpc;
import grpcCode.PeerToPeerOuterClass;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PeerToPeerService extends PeerToPeerGrpc.PeerToPeerImplBase {
    public static Logger LOGGER = LogManager.getLogger(PeerToPeerService.class);

    @Override
    public void ping(PeerToPeerOuterClass.PingInfo request, StreamObserver<PeerToPeerOuterClass.PingSuccess> responseObserver) {
        PeerToPeerOuterClass.NodeInfo nodeDest = request.getDest();
        PeerToPeerOuterClass.NodeInfo nodeOrig = request.getOrig();

        LOGGER.info("Got Ping request");

        PeerToPeerOuterClass.PingSuccess.Builder response =
                PeerToPeerOuterClass.PingSuccess.newBuilder();

        response.setSuccess(isSuccess());

        LOGGER.info("Sending Ping response: Response: " + response.getSuccess());
        responseObserver.onNext(response.build());

        responseObserver.onCompleted();
    }

    private boolean isSuccess() {
        return true;
    }
}
