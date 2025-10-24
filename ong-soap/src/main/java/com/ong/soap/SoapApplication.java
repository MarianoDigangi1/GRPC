package com.ong.soap;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada principal para el microservicio "ong-soap".
 * Este módulo actúa como cliente SOAP (no servidor),
 * invocando el WSDL de la red de ONGs y exponiendo endpoints REST
 * que luego serán consumidos desde el API Gateway (grpc-gateway).
 */
@SpringBootApplication
public class SoapApplication {

    static {
        //  Forzar el provider de JAX-WS 
        System.setProperty("javax.xml.ws.spi.Provider", "com.sun.xml.ws.spi.ProviderImpl");
    }

    public static void main(String[] args) {
        SpringApplication.run(SoapApplication.class, args);
        System.out.println("✅ Servicio SOAP Cliente iniciado correctamente.");
    }
}

