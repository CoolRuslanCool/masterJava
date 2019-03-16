package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableList;
import com.google.common.net.HttpHeaders;
import ru.javaops.masterjava.web.AuthUtil;
import ru.javaops.masterjava.web.WsClient;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.*;

public class AuthenticationHandler implements SOAPHandler<SOAPMessageContext> {
    private final static String USER = WsClient.HOSTS.getString("mail.user");
    private final static String PASSWORD = WsClient.HOSTS.getString("mail.password");

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        if ((boolean)context.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY)) {
            Map<String, List<String>> headers = (Map<String, List<String>>) context.get(SOAPMessageContext.HTTP_REQUEST_HEADERS);
            if (headers == null) {
                headers = new HashMap<>();
                context.put(SOAPMessageContext.HTTP_REQUEST_HEADERS, headers);
            }
            headers.put(HttpHeaders.AUTHORIZATION, ImmutableList.of(AuthUtil.encodeBasicAuthHeader(USER, PASSWORD)));
        }
        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {

    }
}
