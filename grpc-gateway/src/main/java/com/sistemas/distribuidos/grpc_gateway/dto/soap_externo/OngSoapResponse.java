package com.sistemas.distribuidos.grpc_gateway.dto.soap_externo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

// Importamos la clase del MISMO paquete
import com.sistemas.distribuidos.grpc_gateway.dto.soap_externo.OngSoapDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OngSoapResponse {
    
    private List<OngSoapDTO> organizationType;
}
