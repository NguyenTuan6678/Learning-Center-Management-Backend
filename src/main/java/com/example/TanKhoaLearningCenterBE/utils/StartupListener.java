package com.example.TanKhoaLearningCenterBE.utils;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StartupListener implements ApplicationListener<WebServerInitializedEvent> {

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        int port = event.getWebServer().getPort();
        String message = String.format(
            "\n==========================================================\n" +
            "  Application is running!\n" +
            "  Local Server Port: %d\n" +
            "  Swagger UI URL:    http://localhost:%d/swagger-ui/index.html\n" +
            "==========================================================", 
            port, port
        );
        System.out.println(message);
    }
}
