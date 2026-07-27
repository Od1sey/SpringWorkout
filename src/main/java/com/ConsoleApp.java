package com;

import com.controller.BookController;
import com.repository.BookRepo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@ComponentScan("com")
public class ConsoleApp {
    static void main() {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(ConsoleApp.class);
        JdbcTemplate jdbcTemplate = appContext.getBean(JdbcTemplate.class);
        BookController bookController = appContext.getBean(BookController.class);
        BookRepo bookRepo = appContext.getBean(BookRepo.class);

        bookRepo.initiateDatabase();
        bookController.startMainMenu();
        appContext.close();
    }
}
