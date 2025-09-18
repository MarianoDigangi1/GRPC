package com.sistemas.distribuidos.grpc_gateway.service;


import com.sistemas.distribuidos.grpc_gateway.converter.AuthConverter;
import com.sistemas.distribuidos.grpc_gateway.dto.auth.LoginRequestDto;
import com.sistemas.distribuidos.grpc_gateway.dto.auth.LoginResponseDto;
import com.sistemas.distribuidos.grpc_gateway.exception.GrpcConnectionException;
import com.sistemas.distribuidos.grpc_gateway.stubs.LoginRequest;
import com.sistemas.distribuidos.grpc_gateway.stubs.LoginResponse;
import com.sistemas.distribuidos.grpc_gateway.stubs.UsuarioServiceGrpc;
import io.grpc.ManagedChannel;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UsuarioServiceGrpc.UsuarioServiceBlockingStub stubBlocking;

    public AuthService(ManagedChannel grpcChannel) {
        this.stubBlocking = UsuarioServiceGrpc.newBlockingStub(grpcChannel);
    }

    public LoginResponseDto authenticate(LoginRequestDto loginRequestDto) {
        LoginResponse loginResponse;

        try {
            loginResponse = stubBlocking.login(LoginRequest.newBuilder().setIdentificador(loginRequestDto.getIdentificador()).setClave(loginRequestDto.getClave()).build());
        } catch (Exception e) {
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }

        return AuthConverter.convertResponseToDto(loginResponse);
    }

}
