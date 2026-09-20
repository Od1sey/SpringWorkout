package com;

import com.server.WebServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com")
public class ConsoleApp {
    static void main() throws Exception {
        WebServer.start();
    }
}
