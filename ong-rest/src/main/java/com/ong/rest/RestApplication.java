package com.ong.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
        System.out.println("✅ Servidor REST de la ONG 'Empuje Comunitario' iniciado correctamente");
    }
}
