package ru.javaops.masterjava.upload;

import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.GroupDao;

public class GroupProcessor {
    private final GroupDao dao = DBIProvider.getDao(GroupDao.class);

}
