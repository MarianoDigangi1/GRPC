package com.sistemas.distribuidos.grpc_gateway.service;

import com.sistemas.distribuidos.grpc_gateway.converter.SolicitudDonacionesExternasConverter;
import com.sistemas.distribuidos.grpc_gateway.dto.solicitud_donacion_externa.SolicitudDonacionExternaResponseDto;
import com.sistemas.distribuidos.grpc_gateway.dto.solicitud_donacion_externa.SolicitudesExternasAgrupadas;
import com.sistemas.distribuidos.grpc_gateway.exception.GrpcConnectionException;
import com.sistemas.distribuidos.grpc_gateway.stubs.solicitudes_externas.Empty;
import com.sistemas.distribuidos.grpc_gateway.stubs.solicitudes_externas.ListarSolicitudesExternasResponse;
import com.sistemas.distribuidos.grpc_gateway.stubs.solicitudes_externas.SolicitudesExternasServiceGrpc;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudDonacionesExternasService {
    private final SolicitudesExternasServiceGrpc.SolicitudesExternasServiceBlockingStub stubBlocking;

    @Value("${kafka.producer.server.idOrganizacion}")
    private String idOrganizacion;

    @Autowired
    public SolicitudDonacionesExternasService(ManagedChannel grpcChannel) {
        this.stubBlocking = SolicitudesExternasServiceGrpc.newBlockingStub(grpcChannel);
    }

    public List<SolicitudDonacionExternaResponseDto> listarSolicitudes() {
        try {
            ListarSolicitudesExternasResponse response = stubBlocking.listarSolicitudesVigentes(Empty.newBuilder().build());
            return SolicitudDonacionesExternasConverter.convertSolicitudListResponseToDto(response);
        } catch (Exception e) {
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }
    }

    public SolicitudesExternasAgrupadas listarSolicitudesAgrupadas() {
        try {
            ListarSolicitudesExternasResponse response = stubBlocking.listarSolicitudesVigentes(Empty.newBuilder().build());
            List<SolicitudDonacionExternaResponseDto> todasLasSolicitudes = 
                    SolicitudDonacionesExternasConverter.convertSolicitudListResponseToDto(response);
            
            // Convertir idOrganizacion a String para comparar
            String idOrganizacionStr = String.valueOf(idOrganizacion);
            
            // Separar solicitudes propias de externas
            List<SolicitudDonacionExternaResponseDto> solicitudesPropias = todasLasSolicitudes.stream()
                    .filter(solicitud -> idOrganizacionStr.equals(solicitud.getIdOrganizacionExterna()))
                    .collect(Collectors.toList());
            
            List<SolicitudDonacionExternaResponseDto> solicitudesExternas = todasLasSolicitudes.stream()
                    .filter(solicitud -> !idOrganizacionStr.equals(solicitud.getIdOrganizacionExterna()))
                    .collect(Collectors.toList());
            
            return new SolicitudesExternasAgrupadas(solicitudesPropias, solicitudesExternas);
        } catch (Exception e) {
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }
    }

    public SolicitudDonacionExternaResponseDto buscaSolicitudPorId(String idSolicitud) {
        try {
            ListarSolicitudesExternasResponse response = stubBlocking.listarSolicitudesVigentes(Empty.newBuilder().build());
            List<SolicitudDonacionExternaResponseDto> todasLasSolicitudes = 
                    SolicitudDonacionesExternasConverter.convertSolicitudListResponseToDto(response);
            
            return todasLasSolicitudes.stream()
                    .filter(solicitud -> idSolicitud.equals(solicitud.getIdSolicitud()))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            throw new GrpcConnectionException("Error al conectar con gRPC: " + e.getMessage(), e);
        }
    }
}
