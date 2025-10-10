package com.sistemas.distribuidos.grpc_gateway.service;

import com.sistemas.distribuidos.grpc_gateway.converter.UsuarioConverter;
import com.sistemas.distribuidos.grpc_gateway.dto.user.*;
import com.sistemas.distribuidos.grpc_gateway.exception.GrpcConnectionException;

import com.sistemas.distribuidos.grpc_gateway.stubs.usuarios.*;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioServiceGrpc.UsuarioServiceBlockingStub stubBlocking;

    @Autowired
    public UsuarioService(ManagedChannel grpcChannel) {
        this.stubBlocking = UsuarioServiceGrpc.newBlockingStub(grpcChannel);
    }

    public CreateUserResponseDto crearUsuario(CreateUserRequestDto createUserRequestDto) {
        try {
            CreateUserRequest createUserRequest = UsuarioConverter.convertRequestDtoToGrpcClass(createUserRequestDto);
            CreateUserResponse response = stubBlocking.crearUsuario(createUserRequest);
            return UsuarioConverter.convertCreateUserResponseToDto(response);
        } catch (Exception e) {
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }
    }
    public UpdateAndDeleteUserResponseDto modificarUsuario(UpdateUserRequestDto updateUserRequestDto, int idUsuario) {
    try {
        // Convertir el DTO de entrada al mensaje gRPC
        UpdateUserRequest grpcRequest = UsuarioConverter.convertUpdateUserRequestDtoToGrpcClass(updateUserRequestDto, idUsuario);

        // Llamada gRPC al backend Python
        System.out.println("➡️ Enviando a gRPC:");
        System.out.println("ID: " + idUsuario);
        System.out.println("Nombre: " + updateUserRequestDto.getNombre());
        System.out.println("Apellido: " + updateUserRequestDto.getApellido());
        System.out.println("Email: " + updateUserRequestDto.getEmail());

        UpdateAndDeleteUserResponse grpcResponse = stubBlocking.modificarUsuario(grpcRequest);

        // Construir el DTO de respuesta manualmente
        return new UpdateAndDeleteUserResponseDto(
                null, // El backend actualmente no devuelve el usuario completo, solo un mensaje
                grpcResponse.getMensaje()
        );

    } catch (Exception e) {
        throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
    }
}


    public ListarUsuariosResponseDto listarUsuarios() {
        try {
            // Construir la solicitud vacía para el método gRPC
            Empty request = Empty.newBuilder().build();

            // Llamar al método gRPC ListarUsuarios
            ListarUsuariosResponse grpcResponse = stubBlocking.listarUsuarios(request);

            // Mapear la respuesta gRPC al DTO
            List<UserResponseDto> usuarios = grpcResponse.getUsuariosList().stream()
                    .map(usuario -> new UserResponseDto(
                            usuario.getId(),
                            usuario.getNombreUsuario(),
                            usuario.getNombre(),
                            usuario.getApellido(),
                            usuario.getTelefono(),
                            usuario.getEmail(),
                            usuario.getRol(),
                            usuario.getActivo()
                    ))
                    .collect(Collectors.toList());

            return new ListarUsuariosResponseDto(usuarios);
        } catch (Exception e) {
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }
    }

    public UpdateAndDeleteUserResponseDto bajaUsuario(int idUsuario){
        try {
            BajaUsuarioRequest grpcRequest = BajaUsuarioRequest.newBuilder()
                    .setId(idUsuario)
                    .build();

            UpdateAndDeleteUserResponse grpcResponse = stubBlocking.bajaUsuario(grpcRequest);

            return new UpdateAndDeleteUserResponseDto(
                    null,
                    grpcResponse.getMensaje()
            );
        }catch (Exception e) {
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }

    }

    public UserResponseDto traerUsuarioPorId(int idUsuario) {
    try {
        TraerUsuarioPorIdRequest grpcRequest = TraerUsuarioPorIdRequest.newBuilder()
                .setId(idUsuario)
                .build();

        UserResponse grpcResponse = stubBlocking.traerUsuarioPorId(grpcRequest);

        return new UserResponseDto(
                grpcResponse.getId(),
                grpcResponse.getNombreUsuario(),
                grpcResponse.getNombre(),
                grpcResponse.getApellido(),
                grpcResponse.getTelefono(),
                grpcResponse.getEmail(),
                grpcResponse.getRol(),
                grpcResponse.getActivo()
        );
    } catch (Exception e) {
        throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
    }
}












}
