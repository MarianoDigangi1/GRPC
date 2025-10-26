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
            SoapApi service = new SoapApi(new URL(WSDL_URL));
            this.port = service.getApplication();

            Binding binding = ((BindingProvider) this.port).getBinding();
            List<Handler> handlerChain = new ArrayList<>();
            handlerChain.add(new HeaderAuthHandler());
            binding.setHandlerChain(handlerChain);

        } catch (MalformedURLException e) {
            throw new RuntimeException("No se pudo inicializar el cliente SOAP", e);
        }
    }

    public PresidentTypeArray listarPresidentes(List<String> ids) {
        StringArray orgIds = new StringArray();
        if (ids != null) orgIds.getString().addAll(ids);

        try {
            return port.listPresidents(orgIds);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener los presidentes desde el servicio SOAP.", e);
        }
    }

    public OrganizationTypeArray listarAsociaciones(List<String> ids) {
        StringArray orgIds = new StringArray();
        if (ids != null) orgIds.getString().addAll(ids);

        try {
            return port.listAssociations(orgIds);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener las asociaciones desde el servicio SOAP.", e);
        }
    }
}
