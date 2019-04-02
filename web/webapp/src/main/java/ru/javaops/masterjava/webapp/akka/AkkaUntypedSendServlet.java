package ru.javaops.masterjava.webapp.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.service.mail.GroupResult;
import ru.javaops.masterjava.service.mail.util.MailUtils.MailObject;
import ru.javaops.masterjava.util.Exceptions;

import javax.servlet.AsyncContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

import static ru.javaops.masterjava.webapp.WebUtil.createMailObject;
import static ru.javaops.masterjava.webapp.WebUtil.doAndWriteResponse;
import static ru.javaops.masterjava.webapp.akka.AkkaWebappListener.akkaActivator;

@WebServlet(value = "/sendAkkaUntyped", loadOnStartup = 1)
@Slf4j
@MultipartConfig
public class AkkaUntypedSendServlet extends HttpServlet {
    private ActorRef mailActor;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        mailActor = akkaActivator.getActorRef("akka.tcp://MailService@127.0.0.1:2553/user/mail-actor");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        req.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);

        log.info("Start AkkaUntypedSendServlet.");

        AsyncContext asyncContext = req.startAsync();
        ((ExecutorService) req.getServletContext().getAttribute("service")).submit(Exceptions.wrap(() -> {
            log.info("Start async AkkaUntypedSendServlet runnable.");

            doAndWriteResponse(resp, () -> {
                ActorRef webappActor = akkaActivator.startActor(Props.create(WebappActor.class, asyncContext));
                mailActor.tell(createMailObject(req), webappActor);
            });

            log.info("Finish async AkkaUntypedSendServlet runnable.");
        }));

        log.info("Finish AkkaUntypedSendServlet.");
    }

    public static class WebappActor extends AbstractActor {
        private final AsyncContext asyncContext;

        public WebappActor(AsyncContext asyncContext) {
            this.asyncContext = asyncContext;
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder().match(GroupResult.class,
                    groupResult -> {
                        log.info(groupResult.toString());
                        asyncContext.getResponse().getWriter().write(groupResult.toString());
                        asyncContext.complete();
                    })
                    .build();
        }
    }
}