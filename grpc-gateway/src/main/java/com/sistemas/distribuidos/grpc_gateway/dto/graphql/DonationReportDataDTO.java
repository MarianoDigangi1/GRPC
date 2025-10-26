package com.sistemas.distribuidos.grpc_gateway.dto.graphql;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DonationReportDataDTO {

    @JsonProperty("informe_donaciones")
    private List<ReporteDonacionDTO> informeDonaciones;
}