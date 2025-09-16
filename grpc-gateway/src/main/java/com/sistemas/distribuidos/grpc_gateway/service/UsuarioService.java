package com.sistemas.distribuidos.grpc_gateway.service;

import com.sistemas.distribuidos.grpc_gateway.converter.UsuarioConverter;
import com.sistemas.distribuidos.grpc_gateway.dto.user.CreateUserRequestDto;
import com.sistemas.distribuidos.grpc_gateway.dto.user.CreateUserResponseDto;
import com.sistemas.distribuidos.grpc_gateway.dto.user.UpdateUserRequestDto;
import com.sistemas.distribuidos.grpc_gateway.dto.user.UpdateAndDeleteUserResponseDto;
import com.sistemas.distribuidos.grpc_gateway.exception.GrpcConnectionException;
import com.sistemas.distribuidos.grpc_gateway.stubs.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioServiceGrpc.UsuarioServiceBlockingStub stubBlocking;
    private final EnvioMailService mailService;

    public UsuarioService(JavaMailSender mailSender) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50052)
                .usePlaintext()
                .build();
        this.stubBlocking = UsuarioServiceGrpc.newBlockingStub(channel);
        this.mailService = new EnvioMailService(mailSender);
    }

    public CreateUserResponseDto crearUsuario(CreateUserRequestDto createUserRequestDto) {
        try {
            CreateUserRequest createUserRequest = UsuarioConverter.convertRequestDtoToGrpcClass(createUserRequestDto);
            CreateUserResponse response = stubBlocking.crearUsuario(createUserRequest);
            //mailService.enviarEmailUsuario(response);
            return UsuarioConverter.convertCreateUserResponseToDto(response);
        } catch (Exception e) {
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }
    }

    public UpdateAndDeleteUserResponseDto modificarUsuario(UpdateUserRequestDto updateUserRequestDto, int idUsuario) {
        try {
            UpdateUserRequest grpcRequest = UsuarioConverter.convertUpdateUserRequestDtoToGrpcClass(updateUserRequestDto, idUsuario);
            UpdateAndDeleteUserResponse grpcResponse = stubBlocking.modificarUsuario(grpcRequest);
            return UsuarioConverter.convertUpdateAndDeleteUserResponseToDto(grpcResponse);
        } catch (Exception e) {
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }
    }

    public UpdateAndDeleteUserResponseDto eliminarUsuario(int idUsuario) {
        try {
            UpdateAndDeleteUserResponse grpcResponse = stubBlocking.bajaUsuario(BajaUsuarioRequest.newBuilder().setId(idUsuario).build());
            return UsuarioConverter.convertUpdateAndDeleteUserResponseToDto(grpcResponse);
        } catch  (Exception e) {
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }
    }

}
