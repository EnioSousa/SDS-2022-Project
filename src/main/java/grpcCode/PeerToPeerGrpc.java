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
    comments = "Source: PeerToPeer.proto")
public final class PeerToPeerGrpc {

  private PeerToPeerGrpc() {}

  public static final String SERVICE_NAME = "PeerToPeer";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<grpcCode.PeerToPeerOuterClass.NodeInfoMSG,
      grpcCode.PeerToPeerOuterClass.SuccessMSG> getPingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Ping",
      requestType = grpcCode.PeerToPeerOuterClass.NodeInfoMSG.class,
      responseType = grpcCode.PeerToPeerOuterClass.SuccessMSG.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpcCode.PeerToPeerOuterClass.NodeInfoMSG,
      grpcCode.PeerToPeerOuterClass.SuccessMSG> getPingMethod() {
    io.grpc.MethodDescriptor<grpcCode.PeerToPeerOuterClass.NodeInfoMSG, grpcCode.PeerToPeerOuterClass.SuccessMSG> getPingMethod;
    if ((getPingMethod = PeerToPeerGrpc.getPingMethod) == null) {
      synchronized (PeerToPeerGrpc.class) {
        if ((getPingMethod = PeerToPeerGrpc.getPingMethod) == null) {
          PeerToPeerGrpc.getPingMethod = getPingMethod = 
              io.grpc.MethodDescriptor.<grpcCode.PeerToPeerOuterClass.NodeInfoMSG, grpcCode.PeerToPeerOuterClass.SuccessMSG>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "PeerToPeer", "Ping"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.PeerToPeerOuterClass.NodeInfoMSG.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.PeerToPeerOuterClass.SuccessMSG.getDefaultInstance()))
                  .setSchemaDescriptor(new PeerToPeerMethodDescriptorSupplier("Ping"))
                  .build();
          }
        }
     }
     return getPingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpcCode.PeerToPeerOuterClass.FindNodeMSG,
      grpcCode.PeerToPeerOuterClass.NodeInfoMSG> getFindNodeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "FindNode",
      requestType = grpcCode.PeerToPeerOuterClass.FindNodeMSG.class,
      responseType = grpcCode.PeerToPeerOuterClass.NodeInfoMSG.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<grpcCode.PeerToPeerOuterClass.FindNodeMSG,
      grpcCode.PeerToPeerOuterClass.NodeInfoMSG> getFindNodeMethod() {
    io.grpc.MethodDescriptor<grpcCode.PeerToPeerOuterClass.FindNodeMSG, grpcCode.PeerToPeerOuterClass.NodeInfoMSG> getFindNodeMethod;
    if ((getFindNodeMethod = PeerToPeerGrpc.getFindNodeMethod) == null) {
      synchronized (PeerToPeerGrpc.class) {
        if ((getFindNodeMethod = PeerToPeerGrpc.getFindNodeMethod) == null) {
          PeerToPeerGrpc.getFindNodeMethod = getFindNodeMethod = 
              io.grpc.MethodDescriptor.<grpcCode.PeerToPeerOuterClass.FindNodeMSG, grpcCode.PeerToPeerOuterClass.NodeInfoMSG>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "PeerToPeer", "FindNode"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.PeerToPeerOuterClass.FindNodeMSG.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.PeerToPeerOuterClass.NodeInfoMSG.getDefaultInstance()))
                  .setSchemaDescriptor(new PeerToPeerMethodDescriptorSupplier("FindNode"))
                  .build();
          }
        }
     }
     return getFindNodeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpcCode.PeerToPeerOuterClass.SaveMSG,
      grpcCode.PeerToPeerOuterClass.SuccessMSG> getStoreMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Store",
      requestType = grpcCode.PeerToPeerOuterClass.SaveMSG.class,
      responseType = grpcCode.PeerToPeerOuterClass.SuccessMSG.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpcCode.PeerToPeerOuterClass.SaveMSG,
      grpcCode.PeerToPeerOuterClass.SuccessMSG> getStoreMethod() {
    io.grpc.MethodDescriptor<grpcCode.PeerToPeerOuterClass.SaveMSG, grpcCode.PeerToPeerOuterClass.SuccessMSG> getStoreMethod;
    if ((getStoreMethod = PeerToPeerGrpc.getStoreMethod) == null) {
      synchronized (PeerToPeerGrpc.class) {
        if ((getStoreMethod = PeerToPeerGrpc.getStoreMethod) == null) {
          PeerToPeerGrpc.getStoreMethod = getStoreMethod = 
              io.grpc.MethodDescriptor.<grpcCode.PeerToPeerOuterClass.SaveMSG, grpcCode.PeerToPeerOuterClass.SuccessMSG>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "PeerToPeer", "Store"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.PeerToPeerOuterClass.SaveMSG.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.PeerToPeerOuterClass.SuccessMSG.getDefaultInstance()))
                  .setSchemaDescriptor(new PeerToPeerMethodDescriptorSupplier("Store"))
                  .build();
          }
        }
     }
     return getStoreMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpcCode.PeerToPeerOuterClass.FindMSG,
      grpcCode.PeerToPeerOuterClass.FindResponseMSG> getFindMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Find",
      requestType = grpcCode.PeerToPeerOuterClass.FindMSG.class,
      responseType = grpcCode.PeerToPeerOuterClass.FindResponseMSG.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<grpcCode.PeerToPeerOuterClass.FindMSG,
      grpcCode.PeerToPeerOuterClass.FindResponseMSG> getFindMethod() {
    io.grpc.MethodDescriptor<grpcCode.PeerToPeerOuterClass.FindMSG, grpcCode.PeerToPeerOuterClass.FindResponseMSG> getFindMethod;
    if ((getFindMethod = PeerToPeerGrpc.getFindMethod) == null) {
      synchronized (PeerToPeerGrpc.class) {
        if ((getFindMethod = PeerToPeerGrpc.getFindMethod) == null) {
          PeerToPeerGrpc.getFindMethod = getFindMethod = 
              io.grpc.MethodDescriptor.<grpcCode.PeerToPeerOuterClass.FindMSG, grpcCode.PeerToPeerOuterClass.FindResponseMSG>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "PeerToPeer", "Find"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.PeerToPeerOuterClass.FindMSG.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.PeerToPeerOuterClass.FindResponseMSG.getDefaultInstance()))
                  .setSchemaDescriptor(new PeerToPeerMethodDescriptorSupplier("Find"))
                  .build();
          }
        }
     }
     return getFindMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpcCode.PeerToPeerOuterClass.TransactionMSG,
      grpcCode.PeerToPeerOuterClass.SuccessMSG> getSendTransactionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendTransaction",
      requestType = grpcCode.PeerToPeerOuterClass.TransactionMSG.class,
      responseType = grpcCode.PeerToPeerOuterClass.SuccessMSG.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpcCode.PeerToPeerOuterClass.TransactionMSG,
      grpcCode.PeerToPeerOuterClass.SuccessMSG> getSendTransactionMethod() {
    io.grpc.MethodDescriptor<grpcCode.PeerToPeerOuterClass.TransactionMSG, grpcCode.PeerToPeerOuterClass.SuccessMSG> getSendTransactionMethod;
    if ((getSendTransactionMethod = PeerToPeerGrpc.getSendTransactionMethod) == null) {
      synchronized (PeerToPeerGrpc.class) {
        if ((getSendTransactionMethod = PeerToPeerGrpc.getSendTransactionMethod) == null) {
          PeerToPeerGrpc.getSendTransactionMethod = getSendTransactionMethod = 
              io.grpc.MethodDescriptor.<grpcCode.PeerToPeerOuterClass.TransactionMSG, grpcCode.PeerToPeerOuterClass.SuccessMSG>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "PeerToPeer", "SendTransaction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.PeerToPeerOuterClass.TransactionMSG.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.PeerToPeerOuterClass.SuccessMSG.getDefaultInstance()))
                  .setSchemaDescriptor(new PeerToPeerMethodDescriptorSupplier("SendTransaction"))
                  .build();
          }
        }
     }
     return getSendTransactionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpcCode.PeerToPeerOuterClass.InitMSG,
      grpcCode.PeerToPeerOuterClass.InitResponse> getFirstConnMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "FirstConn",
      requestType = grpcCode.PeerToPeerOuterClass.InitMSG.class,
      responseType = grpcCode.PeerToPeerOuterClass.InitResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpcCode.PeerToPeerOuterClass.InitMSG,
      grpcCode.PeerToPeerOuterClass.InitResponse> getFirstConnMethod() {
    io.grpc.MethodDescriptor<grpcCode.PeerToPeerOuterClass.InitMSG, grpcCode.PeerToPeerOuterClass.InitResponse> getFirstConnMethod;
    if ((getFirstConnMethod = PeerToPeerGrpc.getFirstConnMethod) == null) {
      synchronized (PeerToPeerGrpc.class) {
        if ((getFirstConnMethod = PeerToPeerGrpc.getFirstConnMethod) == null) {
          PeerToPeerGrpc.getFirstConnMethod = getFirstConnMethod = 
              io.grpc.MethodDescriptor.<grpcCode.PeerToPeerOuterClass.InitMSG, grpcCode.PeerToPeerOuterClass.InitResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "PeerToPeer", "FirstConn"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.PeerToPeerOuterClass.InitMSG.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.PeerToPeerOuterClass.InitResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new PeerToPeerMethodDescriptorSupplier("FirstConn"))
                  .build();
          }
        }
     }
     return getFirstConnMethod;
  }

  private static volatile io.grpc.MethodDescriptor<grpcCode.PeerToPeerOuterClass.GetIdMSG,
      grpcCode.PeerToPeerOuterClass.GetIdResponse> getGetIDMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetID",
      requestType = grpcCode.PeerToPeerOuterClass.GetIdMSG.class,
      responseType = grpcCode.PeerToPeerOuterClass.GetIdResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpcCode.PeerToPeerOuterClass.GetIdMSG,
      grpcCode.PeerToPeerOuterClass.GetIdResponse> getGetIDMethod() {
    io.grpc.MethodDescriptor<grpcCode.PeerToPeerOuterClass.GetIdMSG, grpcCode.PeerToPeerOuterClass.GetIdResponse> getGetIDMethod;
    if ((getGetIDMethod = PeerToPeerGrpc.getGetIDMethod) == null) {
      synchronized (PeerToPeerGrpc.class) {
        if ((getGetIDMethod = PeerToPeerGrpc.getGetIDMethod) == null) {
          PeerToPeerGrpc.getGetIDMethod = getGetIDMethod = 
              io.grpc.MethodDescriptor.<grpcCode.PeerToPeerOuterClass.GetIdMSG, grpcCode.PeerToPeerOuterClass.GetIdResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "PeerToPeer", "GetID"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.PeerToPeerOuterClass.GetIdMSG.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpcCode.PeerToPeerOuterClass.GetIdResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new PeerToPeerMethodDescriptorSupplier("GetID"))
                  .build();
          }
        }
     }
     return getGetIDMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PeerToPeerStub newStub(io.grpc.Channel channel) {
    return new PeerToPeerStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PeerToPeerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new PeerToPeerBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PeerToPeerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new PeerToPeerFutureStub(channel);
  }

  /**
   */
  public static abstract class PeerToPeerImplBase implements io.grpc.BindableService {

    /**
     */
    public void ping(grpcCode.PeerToPeerOuterClass.NodeInfoMSG request,
        io.grpc.stub.StreamObserver<grpcCode.PeerToPeerOuterClass.SuccessMSG> responseObserver) {
      asyncUnimplementedUnaryCall(getPingMethod(), responseObserver);
    }

    /**
     */
    public void findNode(grpcCode.PeerToPeerOuterClass.FindNodeMSG request,
        io.grpc.stub.StreamObserver<grpcCode.PeerToPeerOuterClass.NodeInfoMSG> responseObserver) {
      asyncUnimplementedUnaryCall(getFindNodeMethod(), responseObserver);
    }

    /**
     */
    public void store(grpcCode.PeerToPeerOuterClass.SaveMSG request,
        io.grpc.stub.StreamObserver<grpcCode.PeerToPeerOuterClass.SuccessMSG> responseObserver) {
      asyncUnimplementedUnaryCall(getStoreMethod(), responseObserver);
    }

    /**
     */
    public void find(grpcCode.PeerToPeerOuterClass.FindMSG request,
        io.grpc.stub.StreamObserver<grpcCode.PeerToPeerOuterClass.FindResponseMSG> responseObserver) {
      asyncUnimplementedUnaryCall(getFindMethod(), responseObserver);
    }

    /**
     */
    public void sendTransaction(grpcCode.PeerToPeerOuterClass.TransactionMSG request,
        io.grpc.stub.StreamObserver<grpcCode.PeerToPeerOuterClass.SuccessMSG> responseObserver) {
      asyncUnimplementedUnaryCall(getSendTransactionMethod(), responseObserver);
    }

    /**
     */
    public void firstConn(grpcCode.PeerToPeerOuterClass.InitMSG request,
        io.grpc.stub.StreamObserver<grpcCode.PeerToPeerOuterClass.InitResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getFirstConnMethod(), responseObserver);
    }

    /**
     */
    public void getID(grpcCode.PeerToPeerOuterClass.GetIdMSG request,
        io.grpc.stub.StreamObserver<grpcCode.PeerToPeerOuterClass.GetIdResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetIDMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getPingMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpcCode.PeerToPeerOuterClass.NodeInfoMSG,
                grpcCode.PeerToPeerOuterClass.SuccessMSG>(
                  this, METHODID_PING)))
          .addMethod(
            getFindNodeMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                grpcCode.PeerToPeerOuterClass.FindNodeMSG,
                grpcCode.PeerToPeerOuterClass.NodeInfoMSG>(
                  this, METHODID_FIND_NODE)))
          .addMethod(
            getStoreMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpcCode.PeerToPeerOuterClass.SaveMSG,
                grpcCode.PeerToPeerOuterClass.SuccessMSG>(
                  this, METHODID_STORE)))
          .addMethod(
            getFindMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                grpcCode.PeerToPeerOuterClass.FindMSG,
                grpcCode.PeerToPeerOuterClass.FindResponseMSG>(
                  this, METHODID_FIND)))
          .addMethod(
            getSendTransactionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpcCode.PeerToPeerOuterClass.TransactionMSG,
                grpcCode.PeerToPeerOuterClass.SuccessMSG>(
                  this, METHODID_SEND_TRANSACTION)))
          .addMethod(
            getFirstConnMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpcCode.PeerToPeerOuterClass.InitMSG,
                grpcCode.PeerToPeerOuterClass.InitResponse>(
                  this, METHODID_FIRST_CONN)))
          .addMethod(
            getGetIDMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpcCode.PeerToPeerOuterClass.GetIdMSG,
                grpcCode.PeerToPeerOuterClass.GetIdResponse>(
                  this, METHODID_GET_ID)))
          .build();
    }
  }

  /**
   */
  public static final class PeerToPeerStub extends io.grpc.stub.AbstractStub<PeerToPeerStub> {
    private PeerToPeerStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PeerToPeerStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PeerToPeerStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PeerToPeerStub(channel, callOptions);
    }

    /**
     */
    public void ping(grpcCode.PeerToPeerOuterClass.NodeInfoMSG request,
        io.grpc.stub.StreamObserver<grpcCode.PeerToPeerOuterClass.SuccessMSG> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void findNode(grpcCode.PeerToPeerOuterClass.FindNodeMSG request,
        io.grpc.stub.StreamObserver<grpcCode.PeerToPeerOuterClass.NodeInfoMSG> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getFindNodeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void store(grpcCode.PeerToPeerOuterClass.SaveMSG request,
        io.grpc.stub.StreamObserver<grpcCode.PeerToPeerOuterClass.SuccessMSG> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getStoreMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void find(grpcCode.PeerToPeerOuterClass.FindMSG request,
        io.grpc.stub.StreamObserver<grpcCode.PeerToPeerOuterClass.FindResponseMSG> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getFindMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendTransaction(grpcCode.PeerToPeerOuterClass.TransactionMSG request,
        io.grpc.stub.StreamObserver<grpcCode.PeerToPeerOuterClass.SuccessMSG> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendTransactionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void firstConn(grpcCode.PeerToPeerOuterClass.InitMSG request,
        io.grpc.stub.StreamObserver<grpcCode.PeerToPeerOuterClass.InitResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getFirstConnMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getID(grpcCode.PeerToPeerOuterClass.GetIdMSG request,
        io.grpc.stub.StreamObserver<grpcCode.PeerToPeerOuterClass.GetIdResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetIDMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class PeerToPeerBlockingStub extends io.grpc.stub.AbstractStub<PeerToPeerBlockingStub> {
    private PeerToPeerBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PeerToPeerBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PeerToPeerBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PeerToPeerBlockingStub(channel, callOptions);
    }

    /**
     */
    public grpcCode.PeerToPeerOuterClass.SuccessMSG ping(grpcCode.PeerToPeerOuterClass.NodeInfoMSG request) {
      return blockingUnaryCall(
          getChannel(), getPingMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<grpcCode.PeerToPeerOuterClass.NodeInfoMSG> findNode(
        grpcCode.PeerToPeerOuterClass.FindNodeMSG request) {
      return blockingServerStreamingCall(
          getChannel(), getFindNodeMethod(), getCallOptions(), request);
    }

    /**
     */
    public grpcCode.PeerToPeerOuterClass.SuccessMSG store(grpcCode.PeerToPeerOuterClass.SaveMSG request) {
      return blockingUnaryCall(
          getChannel(), getStoreMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<grpcCode.PeerToPeerOuterClass.FindResponseMSG> find(
        grpcCode.PeerToPeerOuterClass.FindMSG request) {
      return blockingServerStreamingCall(
          getChannel(), getFindMethod(), getCallOptions(), request);
    }

    /**
     */
    public grpcCode.PeerToPeerOuterClass.SuccessMSG sendTransaction(grpcCode.PeerToPeerOuterClass.TransactionMSG request) {
      return blockingUnaryCall(
          getChannel(), getSendTransactionMethod(), getCallOptions(), request);
    }

    /**
     */
    public grpcCode.PeerToPeerOuterClass.InitResponse firstConn(grpcCode.PeerToPeerOuterClass.InitMSG request) {
      return blockingUnaryCall(
          getChannel(), getFirstConnMethod(), getCallOptions(), request);
    }

    /**
     */
    public grpcCode.PeerToPeerOuterClass.GetIdResponse getID(grpcCode.PeerToPeerOuterClass.GetIdMSG request) {
      return blockingUnaryCall(
          getChannel(), getGetIDMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class PeerToPeerFutureStub extends io.grpc.stub.AbstractStub<PeerToPeerFutureStub> {
    private PeerToPeerFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PeerToPeerFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PeerToPeerFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PeerToPeerFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpcCode.PeerToPeerOuterClass.SuccessMSG> ping(
        grpcCode.PeerToPeerOuterClass.NodeInfoMSG request) {
      return futureUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpcCode.PeerToPeerOuterClass.SuccessMSG> store(
        grpcCode.PeerToPeerOuterClass.SaveMSG request) {
      return futureUnaryCall(
          getChannel().newCall(getStoreMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpcCode.PeerToPeerOuterClass.SuccessMSG> sendTransaction(
        grpcCode.PeerToPeerOuterClass.TransactionMSG request) {
      return futureUnaryCall(
          getChannel().newCall(getSendTransactionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpcCode.PeerToPeerOuterClass.InitResponse> firstConn(
        grpcCode.PeerToPeerOuterClass.InitMSG request) {
      return futureUnaryCall(
          getChannel().newCall(getFirstConnMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpcCode.PeerToPeerOuterClass.GetIdResponse> getID(
        grpcCode.PeerToPeerOuterClass.GetIdMSG request) {
      return futureUnaryCall(
          getChannel().newCall(getGetIDMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PING = 0;
  private static final int METHODID_FIND_NODE = 1;
  private static final int METHODID_STORE = 2;
  private static final int METHODID_FIND = 3;
  private static final int METHODID_SEND_TRANSACTION = 4;
  private static final int METHODID_FIRST_CONN = 5;
  private static final int METHODID_GET_ID = 6;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final PeerToPeerImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(PeerToPeerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PING:
          serviceImpl.ping((grpcCode.PeerToPeerOuterClass.NodeInfoMSG) request,
              (io.grpc.stub.StreamObserver<grpcCode.PeerToPeerOuterClass.SuccessMSG>) responseObserver);
          break;
        case METHODID_FIND_NODE:
          serviceImpl.findNode((grpcCode.PeerToPeerOuterClass.FindNodeMSG) request,
              (io.grpc.stub.StreamObserver<grpcCode.PeerToPeerOuterClass.NodeInfoMSG>) responseObserver);
          break;
        case METHODID_STORE:
          serviceImpl.store((grpcCode.PeerToPeerOuterClass.SaveMSG) request,
              (io.grpc.stub.StreamObserver<grpcCode.PeerToPeerOuterClass.SuccessMSG>) responseObserver);
          break;
        case METHODID_FIND:
          serviceImpl.find((grpcCode.PeerToPeerOuterClass.FindMSG) request,
              (io.grpc.stub.StreamObserver<grpcCode.PeerToPeerOuterClass.FindResponseMSG>) responseObserver);
          break;
        case METHODID_SEND_TRANSACTION:
          serviceImpl.sendTransaction((grpcCode.PeerToPeerOuterClass.TransactionMSG) request,
              (io.grpc.stub.StreamObserver<grpcCode.PeerToPeerOuterClass.SuccessMSG>) responseObserver);
          break;
        case METHODID_FIRST_CONN:
          serviceImpl.firstConn((grpcCode.PeerToPeerOuterClass.InitMSG) request,
              (io.grpc.stub.StreamObserver<grpcCode.PeerToPeerOuterClass.InitResponse>) responseObserver);
          break;
        case METHODID_GET_ID:
          serviceImpl.getID((grpcCode.PeerToPeerOuterClass.GetIdMSG) request,
              (io.grpc.stub.StreamObserver<grpcCode.PeerToPeerOuterClass.GetIdResponse>) responseObserver);
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

  private static abstract class PeerToPeerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    PeerToPeerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return grpcCode.PeerToPeerOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("PeerToPeer");
    }
  }

  private static final class PeerToPeerFileDescriptorSupplier
      extends PeerToPeerBaseDescriptorSupplier {
    PeerToPeerFileDescriptorSupplier() {}
  }

  private static final class PeerToPeerMethodDescriptorSupplier
      extends PeerToPeerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    PeerToPeerMethodDescriptorSupplier(String methodName) {
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
      synchronized (PeerToPeerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new PeerToPeerFileDescriptorSupplier())
              .addMethod(getPingMethod())
              .addMethod(getFindNodeMethod())
              .addMethod(getStoreMethod())
              .addMethod(getFindMethod())
              .addMethod(getSendTransactionMethod())
              .addMethod(getFirstConnMethod())
              .addMethod(getGetIDMethod())
              .build();
        }
      }
    }
    return result;
  }
}
