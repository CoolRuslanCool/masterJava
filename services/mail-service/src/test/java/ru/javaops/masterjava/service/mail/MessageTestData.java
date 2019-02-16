package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.service.mail.dao.model.Message;
import ru.javaops.masterjava.service.mail.dao.model.SendStatus;

import java.sql.Timestamp;
import java.util.Date;

public class MessageTestData {
    public static Message MESSAGE_1;
    public static Message MESSAGE_2;
    public static Message MESSAGE_3;

    public static void init() {
        MESSAGE_1 = new Message(
                "cool3331982@mail.ru, cool61082@gmail.com",
                "empty1",
                "<h2>Hello1</h2>",
                "Subj1",
                new Timestamp(new Date().getTime()),
                SendStatus.OK
        );
        MESSAGE_2 = new Message(
                "cool3331982@mail.ru, cool61082@gmail.com",
                "empty2",
                "<h2>Hello2</h2>",
                "Subj2",
                new Timestamp(new Date().getTime()),
                SendStatus.FAILED
        );
        MESSAGE_3 = new Message(
                "cool3331982@mail.ru, cool61082@gmail.com",
                "empty3",
                "<h2>Hello3</h2>",
                "Subj3",
                new Timestamp(new Date().getTime()),
                SendStatus.OK
        );
    }
}
