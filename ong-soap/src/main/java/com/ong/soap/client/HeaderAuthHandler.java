package com.ong.soap.client;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Set;

/**
 * Este Handler intercepta los mensajes SOAP salientes y les añade
 * el header de autenticación requerido por el servidor.
 * Esto soluciona el error HTTP 500.
 */
public class HeaderAuthHandler implements SOAPHandler<SOAPMessageContext> {

    // Tus credenciales de autenticación
    private static final String GRUPO = "GrupoA-TM";
    private static final String CLAVE = "clave-tm-a";
    
    // Namespace definido en el Postman
    private static final String AUTH_NAMESPACE_URI = "auth.headers";
    private static final String AUTH_NAMESPACE_PREFIX = "auth";


    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        Boolean isOutbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        // Solo nos interesa el mensaje de SALIDA (outbound)
        if (isOutbound) {
            try {
                SOAPMessage soapMessage = context.getMessage();
                SOAPEnvelope soapEnvelope = soapMessage.getSOAPPart().getEnvelope();

                // Obtener o crear el Header
                SOAPHeader header = soapEnvelope.getHeader();
                if (header == null) {
                    header = soapEnvelope.addHeader();
                }

                // 1. Crear el elemento <auth:Auth>
                QName authQName = new QName(AUTH_NAMESPACE_URI, "Auth", AUTH_NAMESPACE_PREFIX);
                SOAPHeaderElement authElement = header.addHeaderElement(authQName);

                // 2. Crear y añadir <auth:Grupo>
                QName grupoQName = new QName(AUTH_NAMESPACE_URI, "Grupo", AUTH_NAMESPACE_PREFIX);
                SOAPElement grupoElement = authElement.addChildElement(grupoQName);
                grupoElement.addTextNode(GRUPO);

                // 3. Crear y añadir <auth:Clave>
                QName claveQName = new QName(AUTH_NAMESPACE_URI, "Clave", AUTH_NAMESPACE_PREFIX);
                SOAPElement claveElement = authElement.addChildElement(claveQName);
                claveElement.addTextNode(CLAVE);

                // Guardar los cambios en el mensaje
                soapMessage.saveChanges();
                
            } catch (Exception e) {
                System.err.println("Error al añadir el header SOAP de autenticación: " + e.getMessage());
                e.printStackTrace();
                return false; // Detener el procesamiento si hay error
            }
        }
        return true; // Continuar con el siguiente handler
    }

    @Override
    public Set<QName> getHeaders() {
        return null; 
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {
    }
}

