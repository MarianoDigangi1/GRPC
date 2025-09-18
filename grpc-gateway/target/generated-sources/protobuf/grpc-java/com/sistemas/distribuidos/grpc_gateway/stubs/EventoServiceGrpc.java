package com.sistemas.distribuidos.grpc_gateway.stubs;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.59.0)",
    comments = "Source: eventos.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class EventoServiceGrpc {

  private EventoServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "eventos.EventoService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoRequest,
      com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoResponse> getCrearEventoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CrearEvento",
      requestType = com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoRequest.class,
      responseType = com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoRequest,
      com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoResponse> getCrearEventoMethod() {
    io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoRequest, com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoResponse> getCrearEventoMethod;
    if ((getCrearEventoMethod = EventoServiceGrpc.getCrearEventoMethod) == null) {
      synchronized (EventoServiceGrpc.class) {
        if ((getCrearEventoMethod = EventoServiceGrpc.getCrearEventoMethod) == null) {
          EventoServiceGrpc.getCrearEventoMethod = getCrearEventoMethod =
              io.grpc.MethodDescriptor.<com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoRequest, com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CrearEvento"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoResponse.getDefaultInstance()))
              .setSchemaDescriptor(new EventoServiceMethodDescriptorSupplier("CrearEvento"))
              .build();
        }
      }
    }
    return getCrearEventoMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoRequest,
      com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoResponse> getModificarEventoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ModificarEvento",
      requestType = com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoRequest.class,
      responseType = com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoRequest,
      com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoResponse> getModificarEventoMethod() {
    io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoRequest, com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoResponse> getModificarEventoMethod;
    if ((getModificarEventoMethod = EventoServiceGrpc.getModificarEventoMethod) == null) {
      synchronized (EventoServiceGrpc.class) {
        if ((getModificarEventoMethod = EventoServiceGrpc.getModificarEventoMethod) == null) {
          EventoServiceGrpc.getModificarEventoMethod = getModificarEventoMethod =
              io.grpc.MethodDescriptor.<com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoRequest, com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ModificarEvento"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoResponse.getDefaultInstance()))
              .setSchemaDescriptor(new EventoServiceMethodDescriptorSupplier("ModificarEvento"))
              .build();
        }
      }
    }
    return getModificarEventoMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoRequest,
      com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoResponse> getBajaEventoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "BajaEvento",
      requestType = com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoRequest.class,
      responseType = com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoRequest,
      com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoResponse> getBajaEventoMethod() {
    io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoRequest, com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoResponse> getBajaEventoMethod;
    if ((getBajaEventoMethod = EventoServiceGrpc.getBajaEventoMethod) == null) {
      synchronized (EventoServiceGrpc.class) {
        if ((getBajaEventoMethod = EventoServiceGrpc.getBajaEventoMethod) == null) {
          EventoServiceGrpc.getBajaEventoMethod = getBajaEventoMethod =
              io.grpc.MethodDescriptor.<com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoRequest, com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "BajaEvento"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoResponse.getDefaultInstance()))
              .setSchemaDescriptor(new EventoServiceMethodDescriptorSupplier("BajaEvento"))
              .build();
        }
      }
    }
    return getBajaEventoMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarRequest,
      com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarResponse> getAsignarOQuitarMiembroMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AsignarOQuitarMiembro",
      requestType = com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarRequest.class,
      responseType = com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarRequest,
      com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarResponse> getAsignarOQuitarMiembroMethod() {
    io.grpc.MethodDescriptor<com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarRequest, com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarResponse> getAsignarOQuitarMiembroMethod;
    if ((getAsignarOQuitarMiembroMethod = EventoServiceGrpc.getAsignarOQuitarMiembroMethod) == null) {
      synchronized (EventoServiceGrpc.class) {
        if ((getAsignarOQuitarMiembroMethod = EventoServiceGrpc.getAsignarOQuitarMiembroMethod) == null) {
          EventoServiceGrpc.getAsignarOQuitarMiembroMethod = getAsignarOQuitarMiembroMethod =
              io.grpc.MethodDescriptor.<com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarRequest, com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AsignarOQuitarMiembro"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarResponse.getDefaultInstance()))
              .setSchemaDescriptor(new EventoServiceMethodDescriptorSupplier("AsignarOQuitarMiembro"))
              .build();
        }
      }
    }
    return getAsignarOQuitarMiembroMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static EventoServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EventoServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EventoServiceStub>() {
        @java.lang.Override
        public EventoServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EventoServiceStub(channel, callOptions);
        }
      };
    return EventoServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static EventoServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EventoServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EventoServiceBlockingStub>() {
        @java.lang.Override
        public EventoServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EventoServiceBlockingStub(channel, callOptions);
        }
      };
    return EventoServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static EventoServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EventoServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EventoServiceFutureStub>() {
        @java.lang.Override
        public EventoServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EventoServiceFutureStub(channel, callOptions);
        }
      };
    return EventoServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void crearEvento(com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoRequest request,
        io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCrearEventoMethod(), responseObserver);
    }

    /**
     */
    default void modificarEvento(com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoRequest request,
        io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getModificarEventoMethod(), responseObserver);
    }

    /**
     */
    default void bajaEvento(com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoRequest request,
        io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getBajaEventoMethod(), responseObserver);
    }

    /**
     */
    default void asignarOQuitarMiembro(com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarRequest request,
        io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAsignarOQuitarMiembroMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service EventoService.
   */
  public static abstract class EventoServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return EventoServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service EventoService.
   */
  public static final class EventoServiceStub
      extends io.grpc.stub.AbstractAsyncStub<EventoServiceStub> {
    private EventoServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EventoServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EventoServiceStub(channel, callOptions);
    }

    /**
     */
    public void crearEvento(com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoRequest request,
        io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCrearEventoMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void modificarEvento(com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoRequest request,
        io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getModificarEventoMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void bajaEvento(com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoRequest request,
        io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getBajaEventoMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void asignarOQuitarMiembro(com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarRequest request,
        io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAsignarOQuitarMiembroMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service EventoService.
   */
  public static final class EventoServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<EventoServiceBlockingStub> {
    private EventoServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EventoServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EventoServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoResponse crearEvento(com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCrearEventoMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoResponse modificarEvento(com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getModificarEventoMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoResponse bajaEvento(com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getBajaEventoMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarResponse asignarOQuitarMiembro(com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAsignarOQuitarMiembroMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service EventoService.
   */
  public static final class EventoServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<EventoServiceFutureStub> {
    private EventoServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EventoServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EventoServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoResponse> crearEvento(
        com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCrearEventoMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoResponse> modificarEvento(
        com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getModificarEventoMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoResponse> bajaEvento(
        com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getBajaEventoMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarResponse> asignarOQuitarMiembro(
        com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAsignarOQuitarMiembroMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREAR_EVENTO = 0;
  private static final int METHODID_MODIFICAR_EVENTO = 1;
  private static final int METHODID_BAJA_EVENTO = 2;
  private static final int METHODID_ASIGNAR_OQUITAR_MIEMBRO = 3;

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
        case METHODID_CREAR_EVENTO:
          serviceImpl.crearEvento((com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoRequest) request,
              (io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoResponse>) responseObserver);
          break;
        case METHODID_MODIFICAR_EVENTO:
          serviceImpl.modificarEvento((com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoRequest) request,
              (io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoResponse>) responseObserver);
          break;
        case METHODID_BAJA_EVENTO:
          serviceImpl.bajaEvento((com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoRequest) request,
              (io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoResponse>) responseObserver);
          break;
        case METHODID_ASIGNAR_OQUITAR_MIEMBRO:
          serviceImpl.asignarOQuitarMiembro((com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarRequest) request,
              (io.grpc.stub.StreamObserver<com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarResponse>) responseObserver);
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
          getCrearEventoMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoRequest,
              com.sistemas.distribuidos.grpc_gateway.stubs.CrearEventoResponse>(
                service, METHODID_CREAR_EVENTO)))
        .addMethod(
          getModificarEventoMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoRequest,
              com.sistemas.distribuidos.grpc_gateway.stubs.ModificarEventoResponse>(
                service, METHODID_MODIFICAR_EVENTO)))
        .addMethod(
          getBajaEventoMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoRequest,
              com.sistemas.distribuidos.grpc_gateway.stubs.BajaEventoResponse>(
                service, METHODID_BAJA_EVENTO)))
        .addMethod(
          getAsignarOQuitarMiembroMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarRequest,
              com.sistemas.distribuidos.grpc_gateway.stubs.AsignarQuitarResponse>(
                service, METHODID_ASIGNAR_OQUITAR_MIEMBRO)))
        .build();
  }

  private static abstract class EventoServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    EventoServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.sistemas.distribuidos.grpc_gateway.stubs.SarasaProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("EventoService");
    }
  }

  private static final class EventoServiceFileDescriptorSupplier
      extends EventoServiceBaseDescriptorSupplier {
    EventoServiceFileDescriptorSupplier() {}
  }

  private static final class EventoServiceMethodDescriptorSupplier
      extends EventoServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    EventoServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (EventoServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new EventoServiceFileDescriptorSupplier())
              .addMethod(getCrearEventoMethod())
              .addMethod(getModificarEventoMethod())
              .addMethod(getBajaEventoMethod())
              .addMethod(getAsignarOQuitarMiembroMethod())
              .build();
        }
      }
    }
    return result;
  }
}
