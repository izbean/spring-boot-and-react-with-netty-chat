package com.izbean.springbootnettychat.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "socket")
@Data
public class SocketProperties {
    private int port;
}
