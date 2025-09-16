package com.sistemas.distribuidos.grpc_gateway.service;


import com.sistemas.distribuidos.grpc_gateway.converter.AuthConverter;
import com.sistemas.distribuidos.grpc_gateway.dto.auth.LoginRequestDto;
import com.sistemas.distribuidos.grpc_gateway.dto.auth.LoginResponseDto;
import com.sistemas.distribuidos.grpc_gateway.stubs.LoginRequest;
import com.sistemas.distribuidos.grpc_gateway.stubs.LoginResponse;
import com.sistemas.distribuidos.grpc_gateway.stubs.UsuarioServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UsuarioServiceGrpc.UsuarioServiceBlockingStub stubBlocking;

    public AuthService(
        @Value("${grpc.usuario.host:localhost}") String grpcHost,
        @Value("${grpc.usuario.port:50052}") int grpcPort
    ) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .build();
        this.stubBlocking = UsuarioServiceGrpc.newBlockingStub(channel);
    }

    //TODO: Falta agregarle try/catch
    public LoginResponseDto authenticate(LoginRequestDto loginRequestDto) {
        LoginResponse loginResponse = stubBlocking.login(LoginRequest.newBuilder().setIdentificador(loginRequestDto.getIdentificador()).setClave(loginRequestDto.getClave()).build());
        return AuthConverter.convertResponseToDto(loginResponse);
    }

}
