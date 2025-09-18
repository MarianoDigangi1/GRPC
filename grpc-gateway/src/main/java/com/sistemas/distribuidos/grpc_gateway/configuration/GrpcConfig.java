package com.sistemas.distribuidos.grpc_gateway.configuration;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {

    @Value("${grpc.server.host:localhost}")
    private String grpcHost;

    @Value("${grpc.server.port:50052}")
    private int grpcPort;

    @Bean
    public ManagedChannel grpcChannel() {
        return ManagedChannelBuilder
                .forAddress(grpcHost, grpcPort)
                .usePlaintext()
                .build();
    }
}
