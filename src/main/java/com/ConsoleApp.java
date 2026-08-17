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
        MainMenuController mainMenuController = appContext.getBean(MainMenuController.class);

        mainMenuController.startMenu();

        appContext.close();
    }
}
