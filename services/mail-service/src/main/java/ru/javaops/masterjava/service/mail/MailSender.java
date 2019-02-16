package ru.javaops.masterjava.service.mail;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.io.File;
import java.util.List;

@Slf4j
public class MailSender {

    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) {
        log.info("Send mail to \'" + to + "\' cc \'" + cc + "\' subject \'" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));

        try {
            final HtmlEmail htmlEmail = MailConfig.INSTANCE.createHtmlEmail();
            htmlEmail.setSubject(subject);
            for (Addressee addressee: to) {
                htmlEmail.addTo(addressee.getEmail(), addressee.getName());
            }
            for (Addressee addressee: to) {
                htmlEmail.addCc(addressee.getEmail(), addressee.getName());
            }

            htmlEmail.setHtmlMsg(body);
            htmlEmail.setTextMsg("Tom tom");
            htmlEmail.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }
}
