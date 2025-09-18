package com.sistemas.distribuidos.grpc_gateway.stubs;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * ---- Servicios ----
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.59.0)",
    comments = "Source: usuarios.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class UsuarioServiceGrpc {

  private UsuarioServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "usuarios.UsuarioService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserRequest,
      com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserResponse> getCrearUsuarioMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CrearUsuario",
      requestType = com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserRequest.class,
      responseType = com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserRequest,
      com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserResponse> getCrearUsuarioMethod() {
    io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserRequest, com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserResponse> getCrearUsuarioMethod;
    if ((getCrearUsuarioMethod = UsuarioServiceGrpc.getCrearUsuarioMethod) == null) {
      synchronized (UsuarioServiceGrpc.class) {
        if ((getCrearUsuarioMethod = UsuarioServiceGrpc.getCrearUsuarioMethod) == null) {
          UsuarioServiceGrpc.getCrearUsuarioMethod = getCrearUsuarioMethod =
              io.grpc.MethodDescriptor.<com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserRequest, com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CrearUsuario"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UsuarioServiceMethodDescriptorSupplier("CrearUsuario"))
              .build();
        }
      }
    }
    return getCrearUsuarioMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.UpdateUserRequest,
      com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse> getModificarUsuarioMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ModificarUsuario",
      requestType = com.sistemas.distribuidos.grpc_gateway.stubs.UpdateUserRequest.class,
      responseType = com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.UpdateUserRequest,
      com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse> getModificarUsuarioMethod() {
    io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.UpdateUserRequest, com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse> getModificarUsuarioMethod;
    if ((getModificarUsuarioMethod = UsuarioServiceGrpc.getModificarUsuarioMethod) == null) {
      synchronized (UsuarioServiceGrpc.class) {
        if ((getModificarUsuarioMethod = UsuarioServiceGrpc.getModificarUsuarioMethod) == null) {
          UsuarioServiceGrpc.getModificarUsuarioMethod = getModificarUsuarioMethod =
              io.grpc.MethodDescriptor.<com.sistemas.distribuidos.grpc_gateway.stubs.UpdateUserRequest, com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ModificarUsuario"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sistemas.distribuidos.grpc_gateway.stubs.UpdateUserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UsuarioServiceMethodDescriptorSupplier("ModificarUsuario"))
              .build();
        }
      }
    }
    return getModificarUsuarioMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.BajaUsuarioRequest,
      com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse> getBajaUsuarioMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "BajaUsuario",
      requestType = com.sistemas.distribuidos.grpc_gateway.stubs.BajaUsuarioRequest.class,
      responseType = com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.BajaUsuarioRequest,
      com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse> getBajaUsuarioMethod() {
    io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.BajaUsuarioRequest, com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse> getBajaUsuarioMethod;
    if ((getBajaUsuarioMethod = UsuarioServiceGrpc.getBajaUsuarioMethod) == null) {
      synchronized (UsuarioServiceGrpc.class) {
        if ((getBajaUsuarioMethod = UsuarioServiceGrpc.getBajaUsuarioMethod) == null) {
          UsuarioServiceGrpc.getBajaUsuarioMethod = getBajaUsuarioMethod =
              io.grpc.MethodDescriptor.<com.sistemas.distribuidos.grpc_gateway.stubs.BajaUsuarioRequest, com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "BajaUsuario"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sistemas.distribuidos.grpc_gateway.stubs.BajaUsuarioRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UsuarioServiceMethodDescriptorSupplier("BajaUsuario"))
              .build();
        }
      }
    }
    return getBajaUsuarioMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.LoginRequest,
      com.sistemas.distribuidos.grpc_gateway.stubs.LoginResponse> getLoginMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Login",
      requestType = com.sistemas.distribuidos.grpc_gateway.stubs.LoginRequest.class,
      responseType = com.sistemas.distribuidos.grpc_gateway.stubs.LoginResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.LoginRequest,
      com.sistemas.distribuidos.grpc_gateway.stubs.LoginResponse> getLoginMethod() {
    io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.LoginRequest, com.sistemas.distribuidos.grpc_gateway.stubs.LoginResponse> getLoginMethod;
    if ((getLoginMethod = UsuarioServiceGrpc.getLoginMethod) == null) {
      synchronized (UsuarioServiceGrpc.class) {
        if ((getLoginMethod = UsuarioServiceGrpc.getLoginMethod) == null) {
          UsuarioServiceGrpc.getLoginMethod = getLoginMethod =
              io.grpc.MethodDescriptor.<com.sistemas.distribuidos.grpc_gateway.stubs.LoginRequest, com.sistemas.distribuidos.grpc_gateway.stubs.LoginResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Login"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sistemas.distribuidos.grpc_gateway.stubs.LoginRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sistemas.distribuidos.grpc_gateway.stubs.LoginResponse.getDefaultInstance()))
              .setSchemaDescriptor(new UsuarioServiceMethodDescriptorSupplier("Login"))
              .build();
        }
      }
    }
    return getLoginMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static UsuarioServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UsuarioServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UsuarioServiceStub>() {
        @java.lang.Override
        public UsuarioServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UsuarioServiceStub(channel, callOptions);
        }
      };
    return UsuarioServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static UsuarioServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UsuarioServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UsuarioServiceBlockingStub>() {
        @java.lang.Override
        public UsuarioServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UsuarioServiceBlockingStub(channel, callOptions);
        }
      };
    return UsuarioServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static UsuarioServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<UsuarioServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<UsuarioServiceFutureStub>() {
        @java.lang.Override
        public UsuarioServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new UsuarioServiceFutureStub(channel, callOptions);
        }
      };
    return UsuarioServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * ---- Servicios ----
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void crearUsuario(com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserRequest request,
        io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCrearUsuarioMethod(), responseObserver);
    }

    /**
     */
    default void modificarUsuario(com.sistemas.distribuidos.grpc_gateway.stubs.UpdateUserRequest request,
        io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getModificarUsuarioMethod(), responseObserver);
    }

    /**
     */
    default void bajaUsuario(com.sistemas.distribuidos.grpc_gateway.stubs.BajaUsuarioRequest request,
        io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getBajaUsuarioMethod(), responseObserver);
    }

    /**
     */
    default void login(com.sistemas.distribuidos.grpc_gateway.stubs.LoginRequest request,
        io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.LoginResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getLoginMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service UsuarioService.
   * <pre>
   * ---- Servicios ----
   * </pre>
   */
  public static abstract class UsuarioServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return UsuarioServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service UsuarioService.
   * <pre>
   * ---- Servicios ----
   * </pre>
   */
  public static final class UsuarioServiceStub
      extends io.grpc.stub.AbstractAsyncStub<UsuarioServiceStub> {
    private UsuarioServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UsuarioServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UsuarioServiceStub(channel, callOptions);
    }

    /**
     */
    public void crearUsuario(com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserRequest request,
        io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCrearUsuarioMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void modificarUsuario(com.sistemas.distribuidos.grpc_gateway.stubs.UpdateUserRequest request,
        io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getModificarUsuarioMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void bajaUsuario(com.sistemas.distribuidos.grpc_gateway.stubs.BajaUsuarioRequest request,
        io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getBajaUsuarioMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void login(com.sistemas.distribuidos.grpc_gateway.stubs.LoginRequest request,
        io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.LoginResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getLoginMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service UsuarioService.
   * <pre>
   * ---- Servicios ----
   * </pre>
   */
  public static final class UsuarioServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<UsuarioServiceBlockingStub> {
    private UsuarioServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UsuarioServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UsuarioServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserResponse crearUsuario(com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCrearUsuarioMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse modificarUsuario(com.sistemas.distribuidos.grpc_gateway.stubs.UpdateUserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getModificarUsuarioMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse bajaUsuario(com.sistemas.distribuidos.grpc_gateway.stubs.BajaUsuarioRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getBajaUsuarioMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.sistemas.distribuidos.grpc_gateway.stubs.LoginResponse login(com.sistemas.distribuidos.grpc_gateway.stubs.LoginRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getLoginMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service UsuarioService.
   * <pre>
   * ---- Servicios ----
   * </pre>
   */
  public static final class UsuarioServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<UsuarioServiceFutureStub> {
    private UsuarioServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected UsuarioServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new UsuarioServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserResponse> crearUsuario(
        com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCrearUsuarioMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse> modificarUsuario(
        com.sistemas.distribuidos.grpc_gateway.stubs.UpdateUserRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getModificarUsuarioMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse> bajaUsuario(
        com.sistemas.distribuidos.grpc_gateway.stubs.BajaUsuarioRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getBajaUsuarioMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.sistemas.distribuidos.grpc_gateway.stubs.LoginResponse> login(
        com.sistemas.distribuidos.grpc_gateway.stubs.LoginRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getLoginMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREAR_USUARIO = 0;
  private static final int METHODID_MODIFICAR_USUARIO = 1;
  private static final int METHODID_BAJA_USUARIO = 2;
  private static final int METHODID_LOGIN = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CREAR_USUARIO:
          serviceImpl.crearUsuario((com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserRequest) request,
              (io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserResponse>) responseObserver);
          break;
        case METHODID_MODIFICAR_USUARIO:
          serviceImpl.modificarUsuario((com.sistemas.distribuidos.grpc_gateway.stubs.UpdateUserRequest) request,
              (io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse>) responseObserver);
          break;
        case METHODID_BAJA_USUARIO:
          serviceImpl.bajaUsuario((com.sistemas.distribuidos.grpc_gateway.stubs.BajaUsuarioRequest) request,
              (io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse>) responseObserver);
          break;
        case METHODID_LOGIN:
          serviceImpl.login((com.sistemas.distribuidos.grpc_gateway.stubs.LoginRequest) request,
              (io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.LoginResponse>) responseObserver);
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

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getCrearUsuarioMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserRequest,
              com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserResponse>(
                service, METHODID_CREAR_USUARIO)))
        .addMethod(
          getModificarUsuarioMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.sistemas.distribuidos.grpc_gateway.stubs.UpdateUserRequest,
              com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse>(
                service, METHODID_MODIFICAR_USUARIO)))
        .addMethod(
          getBajaUsuarioMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.sistemas.distribuidos.grpc_gateway.stubs.BajaUsuarioRequest,
              com.sistemas.distribuidos.grpc_gateway.stubs.UpdateAndDeleteUserResponse>(
                service, METHODID_BAJA_USUARIO)))
        .addMethod(
          getLoginMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.sistemas.distribuidos.grpc_gateway.stubs.LoginRequest,
              com.sistemas.distribuidos.grpc_gateway.stubs.LoginResponse>(
                service, METHODID_LOGIN)))
        .build();
  }

  private static abstract class UsuarioServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    UsuarioServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.sistemas.distribuidos.grpc_gateway.stubs.UsuariosProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("UsuarioService");
    }
  }

  private static final class UsuarioServiceFileDescriptorSupplier
      extends UsuarioServiceBaseDescriptorSupplier {
    UsuarioServiceFileDescriptorSupplier() {}
  }

  private static final class UsuarioServiceMethodDescriptorSupplier
      extends UsuarioServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    UsuarioServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (UsuarioServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new UsuarioServiceFileDescriptorSupplier())
              .addMethod(getCrearUsuarioMethod())
              .addMethod(getModificarUsuarioMethod())
              .addMethod(getBajaUsuarioMethod())
              .addMethod(getLoginMethod())
              .build();
        }
      }
    }
    return result;
  }
}
