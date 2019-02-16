package ru.javaops.masterjava.service.mail;

import com.typesafe.config.Config;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import ru.javaops.masterjava.config.Configs;

import javax.mail.Authenticator;

public class MailConfig {
    public final static MailConfig INSTANCE = new MailConfig(Configs.getConfig("mail.conf", "mail"));

    private final String host;
    private final Integer port;
    private final String username;
    private final Authenticator authenticator;
    private final Boolean useSSL;
    private final Boolean useTLS;
    private final Boolean debug;
    private final String fromName;

    private MailConfig(Config config) {
        this.host = config.getString("host");
        this.port = config.getInt("port");
        this.username = config.getString("username");
        this.authenticator = new DefaultAuthenticator(
                username,
                config.getString("password")
        );
        this.useSSL = config.getBoolean("useSSL");
        this.useTLS = config.getBoolean("useTLS");
        this.debug = config.getBoolean("debug");
        this.fromName = config.getString("fromName");
    }

    public <T extends Email> T prepareEmail(T email) throws EmailException {
        email.setHostName(host);
        if (useSSL) {
            email.setSslSmtpPort(String.valueOf(port));
        } else {
            email.setSmtpPort(port);
        }
        email.setDebug(debug);
        email.setCharset("UTF-8");

        email.setAuthenticator(authenticator);
        email.setSSLOnConnect(useSSL);
        email.setFrom(username, fromName);
        return email;
    }

    public HtmlEmail createHtmlEmail() throws EmailException {
        return prepareEmail(new HtmlEmail());
    }
}
