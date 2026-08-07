package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Scanner;

@Controller
public class MainMenuController {

    private final AuthorController authorController;
    private final BookController bookController;

    @Autowired
    public MainMenuController(AuthorController authorController,
                              BookController bookController) {
        this.authorController = authorController;
        this.bookController = bookController;
    }

    public void startMenu() {
        boolean isRunning = true;
        Scanner scanner = new Scanner(System.in);
        while (isRunning) {
            System.out.print("""
                    
                    === Author menu ===\s
                    Type "book" to start book menu\s
                    Type "author" to start author menu\s
                    Type "exit" to exit the program\s
                    ===========
                    
                    """);
            String input = scanner.nextLine().trim();
            switch (input.toLowerCase()) {
                case "book" -> bookController.startMainMenu();
                case "author" -> authorController.startMainMenu();
                case "exit" -> isRunning = false;
                default -> System.out.printf("Unknown \"%s\" command. Try again.", input);
            }
        }
    }

}
