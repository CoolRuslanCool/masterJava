package ru.javaops.masterjava.service.mail.dao;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.dao.AbstractDaoTest;
import ru.javaops.masterjava.service.mail.MessageTestData;

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
    }

    @Test
    public void selectByStatus() {
    }
}