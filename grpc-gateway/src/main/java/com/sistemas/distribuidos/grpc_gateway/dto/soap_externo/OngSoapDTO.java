package com.sistemas.distribuidos.grpc_gateway.dto.soap_externo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.sistemas.distribuidos.grpc_gateway.dto.JaxbElement;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OngSoapDTO {


    private JaxbElement<java.math.BigInteger> id;
    private JaxbElement<String> name;
    private JaxbElement<String> address;
    private JaxbElement<String> phone;
}

