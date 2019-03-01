package ru.javaops.masterjava.service.mail;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class MailResult {
    public static final String OK = "ok";

    private String email;
    private String result;

    public boolean isOk() {
        return OK.equals(result);
    }

    @Override
    public String toString() {
        return '(' + email + ',' + result + ')';

    }
}
