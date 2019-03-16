package ru.javaops.masterjava.web.handler;

import ru.javaops.masterjava.web.AuthUtil;
import ru.javaops.masterjava.web.WsClient;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SoapServerSecurityHandler implements SOAPHandler<SOAPMessageContext> {

    private static final String USER = WsClient.HOSTS.getString("mail.user");
    private static final String PASSWORD = WsClient.HOSTS.getString("mail.password");

    public static String AUTH_HEADER = AuthUtil.encodeBasicAuthHeader(USER, PASSWORD);

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext context) {
        if (!(boolean)context.get(SOAPMessageContext.MESSAGE_OUTBOUND_PROPERTY)) {
            Map<String, List<String>> headers = (Map<String, List<String>>) context.get(SOAPMessageContext.HTTP_REQUEST_HEADERS);
            int code = AuthUtil.checkBasicAuth(headers, AUTH_HEADER);
            if (code != 0) {
                context.put(MessageContext.HTTP_RESPONSE_CODE, code);
                throw new SecurityException();
            }
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
