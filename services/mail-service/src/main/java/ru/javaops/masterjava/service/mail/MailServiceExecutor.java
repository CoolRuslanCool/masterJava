package ru.javaops.masterjava.service.mail;

import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

@Slf4j
public class MailServiceExecutor {

    private static final String INTERRUPTED_BY_FAULTS_NUMBER = "+++ Interrupted by faults number";
    private static final String INTERRUPTED_BY_TIMEOUT = "+++ Interrupted by timeout";
    private static final String INTERRUPTED_EXCEPTION = "+++ InterruptedException";

    private static final ExecutorService MAIL_EXECUTOR = Executors.newFixedThreadPool(8);

    public static GroupResult sendBulk(final Set<Addressee> addressees, final String subject, final String body) {
        final CompletionService<MailResult> completionService = new ExecutorCompletionService<>(MAIL_EXECUTOR);

        List<Future<MailResult>> futures = StreamEx.of(addressees)
                .map(addressee -> completionService.submit(
                        () -> MailSender.sendTo(addressee, subject, body)))
                .toList();

        return new Callable<GroupResult>() {
            private int success = 0;
            private List<MailResult> failed = new ArrayList<>();

            @Override
            public GroupResult call() {
                while (!futures.isEmpty()) {
                    try {
                        Future<MailResult> future = completionService.poll(10, TimeUnit.SECONDS);
                        if (future == null) {
                            return cancelWithFail(INTERRUPTED_BY_TIMEOUT);
                        }
                        futures.remove(future);
                        MailResult mailResult = future.get();
                        if (mailResult.isOk()) {
                            success++;
                        } else {
                            failed.add(mailResult);
                            if (failed.size() >= 5) {
                                return cancelWithFail(INTERRUPTED_BY_FAULTS_NUMBER);
                            }
                        }

                    } catch (Exception e) {
                        return cancelWithFail(INTERRUPTED_EXCEPTION);
                    }
                }
                GroupResult groupResult = new GroupResult(success, failed, null);
                log.info("groupResult: {}", groupResult);
                return groupResult;

            }

            private GroupResult cancelWithFail(String cause) {
                log.info("Cancel with cause: {}, failed: <{}>.", cause, failed);
                return new GroupResult(success, failed, cause);
            }

        }.call();
    }
}