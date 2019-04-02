package ru.javaops.masterjava.service.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
public class GroupResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private int success; // number of successfully sent email
    private List<MailResult> failed; // failed emails with causes
    private String failedCause;  // global fail cause

    public GroupResult(int success, List<MailResult> failed) {
        this.success = success;
        this.failed = failed;
    }

    @Override
    public String toString() {
        return "Success: " + success + '\n' +
                (failed == null ? "" : "Failed: " + failed.toString() + '\n') +
                (failedCause == null ? "" : "Failed cause: " + failedCause);
    }
}
