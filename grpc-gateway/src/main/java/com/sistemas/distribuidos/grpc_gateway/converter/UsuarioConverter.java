package com.sistemas.distribuidos.grpc_gateway.converter;


import com.sistemas.distribuidos.grpc_gateway.dto.user.*;

import com.sistemas.distribuidos.grpc_gateway.stubs.usuarios.*;

public class UsuarioConverter {

    public static CreateUserRequest convertRequestDtoToGrpcClass(CreateUserRequestDto createUserRequestDto) {
        return CreateUserRequest.newBuilder()
                .setNombreUsuario(createUserRequestDto.getNombreUsuario())
                .setNombre(createUserRequestDto.getNombre())
                .setApellido(createUserRequestDto.getApellido())
                .setTelefono(createUserRequestDto.getTelefono())
                .setEmail(createUserRequestDto.getEmail())
                .setRol(createUserRequestDto.getRol())
                .build();
    }

    public static CreateUserResponseDto convertCreateUserResponseToDto(CreateUserResponse response) {
        UsuarioDto usuarioDto = convertGrpcUsuarioToDto(response.getUsuario());
        return CreateUserResponseDto.builder().usuario(convertGrpcUsuarioToDto(response.getUsuario())).mensaje(response.getMensaje()).build();
    }

    public static UsuarioDto convertGrpcUsuarioToDto(CreateUserRequest usuario) {
        return UsuarioDto.builder().nombreUsuario(usuario.getNombreUsuario())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .telefono(usuario.getTelefono())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .build();
    }

    public static UsuarioDto convertirUsuario(Usuario usuario) {
        return UsuarioDto.builder().nombreUsuario(usuario.getNombreUsuario())
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .telefono(usuario.getTelefono())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .build();
    }

    public static UpdateUserRequest convertUpdateUserRequestDtoToGrpcClass(UpdateUserRequestDto dto, int idUsuario) {
        return UpdateUserRequest.newBuilder()
                .setId(idUsuario)
                .setNombreUsuario(dto.getNombreUsuario())
                .setNombre(dto.getNombre())
                .setApellido(dto.getApellido())
                .setTelefono(dto.getTelefono())
                .setEmail(dto.getEmail())
                .setRol(dto.getRol())
                .setActivo(dto.isActivo())
                .build();
    }

    /*
    public static UpdateAndDeleteUserResponseDto convertUpdateAndDeleteUserResponseToDto(UpdateAndDeleteUserResponse response) {
        UsuarioDto usuarioDto = convertGrpcUsuarioToDto(response.getUsuario());
        return new UpdateAndDeleteUserResponseDto(usuarioDto, response.getMensaje());
    }


    public static UpdateAndDeleteUserResponseDto convertUpdateAndDeleteUserResponseToDto(UpdateAndDeleteUserResponse response) {
        UsuarioDto usuarioDto = convertGrpcUsuarioToDto(response.getUsuario());
        return new UpdateAndDeleteUserResponseDto(usuarioDto, response.getMensaje());
    }
    */
}
