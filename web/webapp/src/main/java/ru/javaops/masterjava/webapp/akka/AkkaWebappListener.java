package ru.javaops.masterjava.webapp.akka;

import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.akka.AkkaActivator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@WebListener
@Slf4j
public class AkkaWebappListener implements ServletContextListener {
    public static AkkaActivator akkaActivator;
    private static ExecutorService service;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        akkaActivator = AkkaActivator.start("WebApp", "webapp");
        service = Executors.newCachedThreadPool();
        sce.getServletContext().setAttribute("service", service);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        akkaActivator.shutdown();
        if (service != null) {
            service.shutdown();
            try {
                if (!service.awaitTermination(3, TimeUnit.SECONDS)) {
                    service.shutdownNow();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}