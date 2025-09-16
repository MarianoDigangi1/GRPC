package com.sistemas.distribuidos.grpc_gateway.exception;

public class GrpcConnectionException extends RuntimeException{
    public static final long serialVersionUID = -7034897190745766939L;

    public GrpcConnectionException() {}

    public GrpcConnectionException(String message) {
        super(message);
    }
    public GrpcConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
