package com.ong.kafka_producer.dto.solicitud_donacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class ResponseDto<T> {
    private T data;
    private boolean isOk;
    private String message;
}
