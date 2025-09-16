package com.sistemas.distribuidos.grpc_gateway.service;

import com.sistemas.distribuidos.grpc_gateway.dto.user.CreateUserRequestDto;
import com.sistemas.distribuidos.grpc_gateway.stubs.CreateUserResponse;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EnvioMailService {
    private final JavaMailSender mailSender;

    public EnvioMailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmailUsuario(CreateUserResponse createUserResponse) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("notificaciones@distribuidos.org");
        message.setTo(createUserResponse.getUsuario().getEmail());
        message.setSubject("Bienvenido");
        //message.setText("Tu contraseña es: " + createUserResponse.getGeneratedPassword() + " no la compartas con nadie 😊");
        mailSender.send(message);
    }
}
