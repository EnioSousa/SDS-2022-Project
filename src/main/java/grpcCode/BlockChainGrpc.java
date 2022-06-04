package grpcCode;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: BlockChain.proto")
public final class BlockChainGrpc {

  private BlockChainGrpc() {}

  public static final String SERVICE_NAME = "BlockChain";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<grpcCode.BlockChainOuterClass.TransactionMSG,
      grpcCode.BlockChainOuterClass.Success> getSendTransactionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendTransaction",
      requestType = grpcCode.BlockChainOuterClass.TransactionMSG.class,
      responseType = grpcCode.BlockChainOuterClass.Success.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpcCode.BlockChainOuterClass.TransactionMSG,
      grpcCode.BlockChainOuterClass.Success> getSendTransactionMethod() {
    io.grpc.MethodDescriptor<grpcCode.BlockChainOuterClass.TransactionMSG, grpcCode.BlockChainOuterClass.Success> getSendTransactionMethod;
    if ((getSendTransactionMethod = BlockChainGrpc.getSendTransactionMethod) == null) {
      synchronized (BlockChainGrpc.class) {
        if ((getSendTransactionMethod = BlockChainGrpc.getSendTransactionMethod) == null) {
          BlockChainGrpc.getSendTransactionMethod = getSendTransactionMethod = 
              io.grpc.MethodDescriptor.<grpcCode.BlockChainOuterClass.TransactionMSG, grpcCode.BlockChainOuterClass.Success>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "BlockChain", "SendTransaction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.BlockChainOuterClass.TransactionMSG.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.BlockChainOuterClass.Success.getDefaultInstance()))
                  .setSchemaDescriptor(new BlockChainMethodDescriptorSupplier("SendTransaction"))
                  .build();
          }
        }
     }
     return getSendTransactionMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BlockChainStub newStub(io.grpc.Channel channel) {
    return new BlockChainStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BlockChainBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new BlockChainBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static BlockChainFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new BlockChainFutureStub(channel);
  }

  /**
   */
  public static abstract class BlockChainImplBase implements io.grpc.BindableService {

    /**
     */
    public void sendTransaction(grpcCode.BlockChainOuterClass.TransactionMSG request,
        io.grpc.stub.StreamObserver<grpcCode.BlockChainOuterClass.Success> responseObserver) {
      asyncUnimplementedUnaryCall(getSendTransactionMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSendTransactionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpcCode.BlockChainOuterClass.TransactionMSG,
                grpcCode.BlockChainOuterClass.Success>(
                  this, METHODID_SEND_TRANSACTION)))
          .build();
    }
  }

  /**
   */
  public static final class BlockChainStub extends io.grpc.stub.AbstractStub<BlockChainStub> {
    private BlockChainStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BlockChainStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BlockChainStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BlockChainStub(channel, callOptions);
    }

    /**
     */
    public void sendTransaction(grpcCode.BlockChainOuterClass.TransactionMSG request,
        io.grpc.stub.StreamObserver<grpcCode.BlockChainOuterClass.Success> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendTransactionMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class BlockChainBlockingStub extends io.grpc.stub.AbstractStub<BlockChainBlockingStub> {
    private BlockChainBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BlockChainBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BlockChainBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BlockChainBlockingStub(channel, callOptions);
    }

    /**
     */
    public grpcCode.BlockChainOuterClass.Success sendTransaction(grpcCode.BlockChainOuterClass.TransactionMSG request) {
      return blockingUnaryCall(
          getChannel(), getSendTransactionMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class BlockChainFutureStub extends io.grpc.stub.AbstractStub<BlockChainFutureStub> {
    private BlockChainFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private BlockChainFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BlockChainFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new BlockChainFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpcCode.BlockChainOuterClass.Success> sendTransaction(
        grpcCode.BlockChainOuterClass.TransactionMSG request) {
      return futureUnaryCall(
          getChannel().newCall(getSendTransactionMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SEND_TRANSACTION = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final BlockChainImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(BlockChainImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEND_TRANSACTION:
          serviceImpl.sendTransaction((grpcCode.BlockChainOuterClass.TransactionMSG) request,
              (io.grpc.stub.StreamObserver<grpcCode.BlockChainOuterClass.Success>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class BlockChainBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    BlockChainBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return grpcCode.BlockChainOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("BlockChain");
    }
  }

  private static final class BlockChainFileDescriptorSupplier
      extends BlockChainBaseDescriptorSupplier {
    BlockChainFileDescriptorSupplier() {}
  }

  private static final class BlockChainMethodDescriptorSupplier
      extends BlockChainBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    BlockChainMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (BlockChainGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new BlockChainFileDescriptorSupplier())
              .addMethod(getSendTransactionMethod())
              .build();
        }
      }
    }
    return result;
  }
}
