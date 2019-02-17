package ru.javaops.masterjava.service.mail.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.dao.AbstractDaoTest;
import ru.javaops.masterjava.service.mail.MessageTestData;
import ru.javaops.masterjava.service.mail.dao.model.Message;
import ru.javaops.masterjava.service.mail.dao.model.SendStatus;

import java.util.List;

import static ru.javaops.masterjava.service.mail.MessageTestData.MESSAGE_1;
import static ru.javaops.masterjava.service.mail.MessageTestData.MESSAGE_2;
import static ru.javaops.masterjava.service.mail.MessageTestData.MESSAGE_3;

public class MessageDaoTest extends AbstractDaoTest<MessageDao> {

    public MessageDaoTest() {
        super(MessageDao.class);
    }

    @BeforeClass
    public static void init() {
        MessageTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        dao.clean();
    }

    @Test
    public void insert() {
        dao.insert(MESSAGE_1);
        final List<Message> messages = dao.selectAll();

        Assert.assertEquals(1, messages.size());
    }

    @Test
    public void selectByStatus() {
        dao.insert(MESSAGE_1);
        dao.insert(MESSAGE_2);
        dao.insert(MESSAGE_3);

        final List<Message> failedMessages = dao.selectByStatus(SendStatus.FAILED);
        final List<Message> sendedMessages = dao.selectByStatus(SendStatus.OK);

        Assert.assertEquals(1, failedMessages.size());
        Assert.assertEquals(2, sendedMessages.size());
    }
}