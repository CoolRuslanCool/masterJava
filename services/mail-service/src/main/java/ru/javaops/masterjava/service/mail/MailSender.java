package ru.javaops.masterjava.service.mail;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.service.mail.dao.MessageDao;
import ru.javaops.masterjava.service.mail.dao.model.Message;
import ru.javaops.masterjava.service.mail.dao.model.SendStatus;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Slf4j
public class MailSender {
    private static final MessageDao DAO = DBIProvider.getDao(MessageDao.class);

    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) {
        log.info("Send mail to \'" + to + "\' cc \'" + cc + "\' subject \'" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));

        final Message message = new Message(
                Joiner.on(", ").join(to.iterator()),
                Joiner.on(", ").join(cc.iterator()),
                body,
                subject,
                new Timestamp(new Date().getTime()),
                SendStatus.OK
        );

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

            DAO.insert(message);
        } catch (EmailException e) {
            log.warn("Failed send mail to \'" + to + "\' cc \'" + cc + "\' subject \'" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));

            message.setStatus(SendStatus.FAILED);
            DAO.insert(message);
        }
    }
}
