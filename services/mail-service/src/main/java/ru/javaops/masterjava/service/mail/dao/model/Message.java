package ru.javaops.masterjava.service.mail.dao.model;


import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @NonNull
    @Column("emails_to")
    private String emailsTo;
    @NonNull
    @Column("emails_cc")
    private String emailsCc;
    @NonNull
    private String body;
    @NonNull
    private String subject;
    @NonNull
    private Timestamp date;
    @NonNull
    private SendStatus status;
}
