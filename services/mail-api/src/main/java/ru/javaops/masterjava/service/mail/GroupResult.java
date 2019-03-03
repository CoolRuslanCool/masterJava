package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class GroupResult {
    private int success; // number of successfully sent email
    private List<MailResult> failed = ImmutableList.of(); // failed emails with causes
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
