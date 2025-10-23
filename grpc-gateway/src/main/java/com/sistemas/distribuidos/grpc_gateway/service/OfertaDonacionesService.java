package com.sistemas.distribuidos.grpc_gateway.service;

import com.sistemas.distribuidos.grpc_gateway.converter.OfertaDonacionesConverter;
import com.sistemas.distribuidos.grpc_gateway.dto.oferta_donaciones.OfertaDonacionResponseDto;
import com.sistemas.distribuidos.grpc_gateway.exception.GrpcConnectionException;
import com.sistemas.distribuidos.grpc_gateway.stubs.donaciones_ofrecidas.DonacionesOfrecidasServiceGrpc;
import com.sistemas.distribuidos.grpc_gateway.stubs.donaciones_ofrecidas.Empty;
import com.sistemas.distribuidos.grpc_gateway.stubs.donaciones_ofrecidas.ListarDonacionesOfrecidasResponse;

import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfertaDonacionesService {
    private final DonacionesOfrecidasServiceGrpc.DonacionesOfrecidasServiceBlockingStub stubBlocking;

    @Autowired
    public OfertaDonacionesService(ManagedChannel grpcChannel) {
        this.stubBlocking = DonacionesOfrecidasServiceGrpc.newBlockingStub(grpcChannel);
    }

    public List<OfertaDonacionResponseDto> listarSolicitudes() {
        try {
            ListarDonacionesOfrecidasResponse response = stubBlocking.listarSolicitudesVigentes(Empty.newBuilder().build());
            return OfertaDonacionesConverter.convertSolicitudListResponseToDto(response);
        } catch (Exception e) {
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }
    }

}
