package com.sistemas.distribuidos.grpc_gateway.dto.soap_externo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sistemas.distribuidos.grpc_gateway.dto.JaxbElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PresidenteSoapDTO {
    
    private JaxbElement<BigInteger> id;
    private JaxbElement<String> name;
    private JaxbElement<String> address;
    private JaxbElement<String> phone;
    
    @JsonProperty("organizationId")
    private JaxbElement<BigInteger> organizationId;
}
