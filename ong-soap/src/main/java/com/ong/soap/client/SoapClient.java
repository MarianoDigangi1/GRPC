package com.ong.soap.client;

import com.ong.soap.wsdl.Application;
import com.ong.soap.wsdl.OrganizationTypeArray;
import com.ong.soap.wsdl.PresidentTypeArray;
import com.ong.soap.wsdl.SoapApi;
import com.ong.soap.wsdl.StringArray;
import org.springframework.stereotype.Component;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


@Component
public class SoapClient {

    private Application port;
    private static final String WSDL_URL = "https://soap-app-latest.onrender.com/?wsdl";

    public SoapClient() {
        try {
            // 1. Crear la instancia del servicio
            SoapApi service = new SoapApi(new URL(WSDL_URL));
            this.port = service.getApplication();

            // 2. OBTENER LA CADENA DE HANDLERS E INYECTAR EL NUESTRO
            Binding binding = ((BindingProvider) this.port).getBinding();
            List<Handler> handlerChain = new ArrayList<>();
            handlerChain.add(new HeaderAuthHandler()); // <-- ¡La magia está aquí!
            binding.setHandlerChain(handlerChain);

        } catch (MalformedURLException e) {
            System.err.println("Error: URL del WSDL mal formada: " + WSDL_URL);
            e.printStackTrace();
            throw new RuntimeException("No se pudo inicializar el cliente SOAP", e);
        }
    }


    public PresidentTypeArray listarPresidentes(List<String> ids) {
        System.out.println("SOAP Client: Enviando solicitud para listar presidentes...");
        

        StringArray orgIds = new StringArray();
        if (ids != null) {
            orgIds.getString().addAll(ids);
        }
        
        return port.listPresidents(orgIds);
    }


    public OrganizationTypeArray listarAsociaciones(List<String> ids) {
        System.out.println("SOAP Client: Enviando solicitud para listar asociaciones...");
        

        StringArray orgIds = new StringArray();
        if (ids != null) {
            orgIds.getString().addAll(ids);
        }

        return port.listAssociations(orgIds);
    }
}

