package com.izbean.springbootnettychat.config;

import com.izbean.springbootnettychat.config.socket.WebSocketServer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ApplicationStartupConfig implements ApplicationListener<ApplicationReadyEvent> {

    private final WebSocketServer webSocketServer;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        webSocketServer.run();
    }
}
