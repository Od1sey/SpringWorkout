package com;

import com.controller.MainMenuController;
import com.repository.AuthorRepo;
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
        BookRepo bookRepo = appContext.getBean(BookRepo.class);
        AuthorRepo authorRepo = appContext.getBean(AuthorRepo.class);
        MainMenuController mainMenuController = appContext.getBean(MainMenuController.class);

        bookRepo.initiateDatabase();
        authorRepo.initiateDatabase();

        mainMenuController.startMenu();

        appContext.close();
    }
}
