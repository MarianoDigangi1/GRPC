package com.sistemas.distribuidos.grpc_gateway.dto.soap_externo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

// Importamos la clase del MISMO paquete
import com.sistemas.distribuidos.grpc_gateway.dto.soap_externo.PresidenteSoapDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PresidenteSoapResponse {
    
    private List<PresidenteSoapDTO> presidentType;
}
