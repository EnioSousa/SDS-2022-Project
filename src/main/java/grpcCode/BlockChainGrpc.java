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
      grpcCode.BlockChainOuterClass.SuccessMSG> getSendTransactionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendTransaction",
      requestType = grpcCode.BlockChainOuterClass.TransactionMSG.class,
      responseType = grpcCode.BlockChainOuterClass.SuccessMSG.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpcCode.BlockChainOuterClass.TransactionMSG,
      grpcCode.BlockChainOuterClass.SuccessMSG> getSendTransactionMethod() {
    io.grpc.MethodDescriptor<grpcCode.BlockChainOuterClass.TransactionMSG, grpcCode.BlockChainOuterClass.SuccessMSG> getSendTransactionMethod;
    if ((getSendTransactionMethod = BlockChainGrpc.getSendTransactionMethod) == null) {
      synchronized (BlockChainGrpc.class) {
        if ((getSendTransactionMethod = BlockChainGrpc.getSendTransactionMethod) == null) {
          BlockChainGrpc.getSendTransactionMethod = getSendTransactionMethod = 
              io.grpc.MethodDescriptor.<grpcCode.BlockChainOuterClass.TransactionMSG, grpcCode.BlockChainOuterClass.SuccessMSG>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "BlockChain", "SendTransaction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.BlockChainOuterClass.TransactionMSG.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.BlockChainOuterClass.SuccessMSG.getDefaultInstance()))
                  .setSchemaDescriptor(new BlockChainMethodDescriptorSupplier("SendTransaction"))
                  .build();
          }
        }
     }
     return getSendTransactionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpcCode.BlockChainOuterClass.BlockMSG,
      grpcCode.BlockChainOuterClass.SuccessMSG> getSendBlockMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendBlock",
      requestType = grpcCode.BlockChainOuterClass.BlockMSG.class,
      responseType = grpcCode.BlockChainOuterClass.SuccessMSG.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpcCode.BlockChainOuterClass.BlockMSG,
      grpcCode.BlockChainOuterClass.SuccessMSG> getSendBlockMethod() {
    io.grpc.MethodDescriptor<grpcCode.BlockChainOuterClass.BlockMSG, grpcCode.BlockChainOuterClass.SuccessMSG> getSendBlockMethod;
    if ((getSendBlockMethod = BlockChainGrpc.getSendBlockMethod) == null) {
      synchronized (BlockChainGrpc.class) {
        if ((getSendBlockMethod = BlockChainGrpc.getSendBlockMethod) == null) {
          BlockChainGrpc.getSendBlockMethod = getSendBlockMethod = 
              io.grpc.MethodDescriptor.<grpcCode.BlockChainOuterClass.BlockMSG, grpcCode.BlockChainOuterClass.SuccessMSG>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "BlockChain", "SendBlock"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.BlockChainOuterClass.BlockMSG.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.BlockChainOuterClass.SuccessMSG.getDefaultInstance()))
                  .setSchemaDescriptor(new BlockChainMethodDescriptorSupplier("SendBlock"))
                  .build();
          }
        }
     }
     return getSendBlockMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpcCode.BlockChainOuterClass.NodeInfoMSG,
      grpcCode.BlockChainOuterClass.blockChainSizeMSG> getSendBlockChainSizeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendBlockChainSize",
      requestType = grpcCode.BlockChainOuterClass.NodeInfoMSG.class,
      responseType = grpcCode.BlockChainOuterClass.blockChainSizeMSG.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpcCode.BlockChainOuterClass.NodeInfoMSG,
      grpcCode.BlockChainOuterClass.blockChainSizeMSG> getSendBlockChainSizeMethod() {
    io.grpc.MethodDescriptor<grpcCode.BlockChainOuterClass.NodeInfoMSG, grpcCode.BlockChainOuterClass.blockChainSizeMSG> getSendBlockChainSizeMethod;
    if ((getSendBlockChainSizeMethod = BlockChainGrpc.getSendBlockChainSizeMethod) == null) {
      synchronized (BlockChainGrpc.class) {
        if ((getSendBlockChainSizeMethod = BlockChainGrpc.getSendBlockChainSizeMethod) == null) {
          BlockChainGrpc.getSendBlockChainSizeMethod = getSendBlockChainSizeMethod = 
              io.grpc.MethodDescriptor.<grpcCode.BlockChainOuterClass.NodeInfoMSG, grpcCode.BlockChainOuterClass.blockChainSizeMSG>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "BlockChain", "SendBlockChainSize"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.BlockChainOuterClass.NodeInfoMSG.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.BlockChainOuterClass.blockChainSizeMSG.getDefaultInstance()))
                  .setSchemaDescriptor(new BlockChainMethodDescriptorSupplier("SendBlockChainSize"))
                  .build();
          }
        }
     }
     return getSendBlockChainSizeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpcCode.BlockChainOuterClass.NodeInfoMSG,
      grpcCode.BlockChainOuterClass.BlockContentMSG> getSendFullBlockChainMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendFullBlockChain",
      requestType = grpcCode.BlockChainOuterClass.NodeInfoMSG.class,
      responseType = grpcCode.BlockChainOuterClass.BlockContentMSG.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<grpcCode.BlockChainOuterClass.NodeInfoMSG,
      grpcCode.BlockChainOuterClass.BlockContentMSG> getSendFullBlockChainMethod() {
    io.grpc.MethodDescriptor<grpcCode.BlockChainOuterClass.NodeInfoMSG, grpcCode.BlockChainOuterClass.BlockContentMSG> getSendFullBlockChainMethod;
    if ((getSendFullBlockChainMethod = BlockChainGrpc.getSendFullBlockChainMethod) == null) {
      synchronized (BlockChainGrpc.class) {
        if ((getSendFullBlockChainMethod = BlockChainGrpc.getSendFullBlockChainMethod) == null) {
          BlockChainGrpc.getSendFullBlockChainMethod = getSendFullBlockChainMethod = 
              io.grpc.MethodDescriptor.<grpcCode.BlockChainOuterClass.NodeInfoMSG, grpcCode.BlockChainOuterClass.BlockContentMSG>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "BlockChain", "SendFullBlockChain"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.BlockChainOuterClass.NodeInfoMSG.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.BlockChainOuterClass.BlockContentMSG.getDefaultInstance()))
                  .setSchemaDescriptor(new BlockChainMethodDescriptorSupplier("SendFullBlockChain"))
                  .build();
          }
        }
     }
     return getSendFullBlockChainMethod;
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
        io.grpc.stub.StreamObserver<grpcCode.BlockChainOuterClass.SuccessMSG> responseObserver) {
      asyncUnimplementedUnaryCall(getSendTransactionMethod(), responseObserver);
    }

    /**
     */
    public void sendBlock(grpcCode.BlockChainOuterClass.BlockMSG request,
        io.grpc.stub.StreamObserver<grpcCode.BlockChainOuterClass.SuccessMSG> responseObserver) {
      asyncUnimplementedUnaryCall(getSendBlockMethod(), responseObserver);
    }

    /**
     */
    public void sendBlockChainSize(grpcCode.BlockChainOuterClass.NodeInfoMSG request,
        io.grpc.stub.StreamObserver<grpcCode.BlockChainOuterClass.blockChainSizeMSG> responseObserver) {
      asyncUnimplementedUnaryCall(getSendBlockChainSizeMethod(), responseObserver);
    }

    /**
     */
    public void sendFullBlockChain(grpcCode.BlockChainOuterClass.NodeInfoMSG request,
        io.grpc.stub.StreamObserver<grpcCode.BlockChainOuterClass.BlockContentMSG> responseObserver) {
      asyncUnimplementedUnaryCall(getSendFullBlockChainMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSendTransactionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpcCode.BlockChainOuterClass.TransactionMSG,
                grpcCode.BlockChainOuterClass.SuccessMSG>(
                  this, METHODID_SEND_TRANSACTION)))
          .addMethod(
            getSendBlockMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpcCode.BlockChainOuterClass.BlockMSG,
                grpcCode.BlockChainOuterClass.SuccessMSG>(
                  this, METHODID_SEND_BLOCK)))
          .addMethod(
            getSendBlockChainSizeMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpcCode.BlockChainOuterClass.NodeInfoMSG,
                grpcCode.BlockChainOuterClass.blockChainSizeMSG>(
                  this, METHODID_SEND_BLOCK_CHAIN_SIZE)))
          .addMethod(
            getSendFullBlockChainMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                grpcCode.BlockChainOuterClass.NodeInfoMSG,
                grpcCode.BlockChainOuterClass.BlockContentMSG>(
                  this, METHODID_SEND_FULL_BLOCK_CHAIN)))
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
        io.grpc.stub.StreamObserver<grpcCode.BlockChainOuterClass.SuccessMSG> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendTransactionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendBlock(grpcCode.BlockChainOuterClass.BlockMSG request,
        io.grpc.stub.StreamObserver<grpcCode.BlockChainOuterClass.SuccessMSG> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendBlockMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendBlockChainSize(grpcCode.BlockChainOuterClass.NodeInfoMSG request,
        io.grpc.stub.StreamObserver<grpcCode.BlockChainOuterClass.blockChainSizeMSG> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendBlockChainSizeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendFullBlockChain(grpcCode.BlockChainOuterClass.NodeInfoMSG request,
        io.grpc.stub.StreamObserver<grpcCode.BlockChainOuterClass.BlockContentMSG> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getSendFullBlockChainMethod(), getCallOptions()), request, responseObserver);
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
    public grpcCode.BlockChainOuterClass.SuccessMSG sendTransaction(grpcCode.BlockChainOuterClass.TransactionMSG request) {
      return blockingUnaryCall(
          getChannel(), getSendTransactionMethod(), getCallOptions(), request);
    }

    /**
     */
    public grpcCode.BlockChainOuterClass.SuccessMSG sendBlock(grpcCode.BlockChainOuterClass.BlockMSG request) {
      return blockingUnaryCall(
          getChannel(), getSendBlockMethod(), getCallOptions(), request);
    }

    /**
     */
    public grpcCode.BlockChainOuterClass.blockChainSizeMSG sendBlockChainSize(grpcCode.BlockChainOuterClass.NodeInfoMSG request) {
      return blockingUnaryCall(
          getChannel(), getSendBlockChainSizeMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<grpcCode.BlockChainOuterClass.BlockContentMSG> sendFullBlockChain(
        grpcCode.BlockChainOuterClass.NodeInfoMSG request) {
      return blockingServerStreamingCall(
          getChannel(), getSendFullBlockChainMethod(), getCallOptions(), request);
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
    public com.google.common.util.concurrent.ListenableFuture<grpcCode.BlockChainOuterClass.SuccessMSG> sendTransaction(
        grpcCode.BlockChainOuterClass.TransactionMSG request) {
      return futureUnaryCall(
          getChannel().newCall(getSendTransactionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpcCode.BlockChainOuterClass.SuccessMSG> sendBlock(
        grpcCode.BlockChainOuterClass.BlockMSG request) {
      return futureUnaryCall(
          getChannel().newCall(getSendBlockMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpcCode.BlockChainOuterClass.blockChainSizeMSG> sendBlockChainSize(
        grpcCode.BlockChainOuterClass.NodeInfoMSG request) {
      return futureUnaryCall(
          getChannel().newCall(getSendBlockChainSizeMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SEND_TRANSACTION = 0;
  private static final int METHODID_SEND_BLOCK = 1;
  private static final int METHODID_SEND_BLOCK_CHAIN_SIZE = 2;
  private static final int METHODID_SEND_FULL_BLOCK_CHAIN = 3;

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
              (io.grpc.stub.StreamObserver<grpcCode.BlockChainOuterClass.SuccessMSG>) responseObserver);
          break;
        case METHODID_SEND_BLOCK:
          serviceImpl.sendBlock((grpcCode.BlockChainOuterClass.BlockMSG) request,
              (io.grpc.stub.StreamObserver<grpcCode.BlockChainOuterClass.SuccessMSG>) responseObserver);
          break;
        case METHODID_SEND_BLOCK_CHAIN_SIZE:
          serviceImpl.sendBlockChainSize((grpcCode.BlockChainOuterClass.NodeInfoMSG) request,
              (io.grpc.stub.StreamObserver<grpcCode.BlockChainOuterClass.blockChainSizeMSG>) responseObserver);
          break;
        case METHODID_SEND_FULL_BLOCK_CHAIN:
          serviceImpl.sendFullBlockChain((grpcCode.BlockChainOuterClass.NodeInfoMSG) request,
              (io.grpc.stub.StreamObserver<grpcCode.BlockChainOuterClass.BlockContentMSG>) responseObserver);
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
              .addMethod(getSendBlockMethod())
              .addMethod(getSendBlockChainSizeMethod())
              .addMethod(getSendFullBlockChainMethod())
              .build();
        }
      }
    }
    return result;
  }
}
