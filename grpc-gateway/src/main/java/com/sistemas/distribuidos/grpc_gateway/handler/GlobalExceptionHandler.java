package com.sistemas.distribuidos.grpc_gateway.handler;

import com.sistemas.distribuidos.grpc_gateway.dto.ErrorResponseDto;
import com.sistemas.distribuidos.grpc_gateway.exception.GrpcConnectionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GrpcConnectionException.class)
    public ResponseEntity<ErrorResponseDto> handleGrpcError(GrpcConnectionException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(new ErrorResponseDto("Error " + HttpStatus.BAD_GATEWAY.value(), ex.getMessage()));
    }
}
