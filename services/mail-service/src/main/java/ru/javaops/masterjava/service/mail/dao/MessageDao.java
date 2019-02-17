package ru.javaops.masterjava.service.mail.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.dao.AbstractDao;
import ru.javaops.masterjava.service.mail.dao.model.Message;
import ru.javaops.masterjava.service.mail.dao.model.SendStatus;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class MessageDao implements AbstractDao {

    @SqlUpdate("insert into messages_history (emails_to, emails_cc, body, subject, date, status) VALUES (:emailsTo, :emailsCc, :body, :subject, :date, cast(:status as send_status)) ")
    public abstract void insert(@BindBean Message message);

    @SqlQuery("select * from messages_history where status=cast(:it as send_status)")
    public abstract List<Message> selectByStatus(@Bind SendStatus status);

    @SqlQuery("select * from messages_history ")
    public abstract List<Message> selectAll();

    @Override
    @SqlUpdate("truncate messages_history")
    public abstract void clean();
}
