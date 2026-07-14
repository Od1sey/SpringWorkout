package com;

import com.contoller.BookController;
import com.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com")
public class ConsoleApp {
    static void main() {
        ApplicationContext appContext = new AnnotationConfigApplicationContext(ConsoleApp.class);
        BookController bookController = appContext.getBean(BookController.class);

        bookController.startMenu();
    }
}
