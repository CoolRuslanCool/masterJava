package ru.javaops.masterjava.service.mail.dao;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.service.mail.MessageTestData;

import static org.junit.Assert.*;

public class MessageDaoTest {

    public MessageDaoTest() {
    }

    @BeforeClass
    public static void init() {
        MessageTestData.init();
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void insert() {
    }

    @Test
    public void selectByStatus() {
    }
}