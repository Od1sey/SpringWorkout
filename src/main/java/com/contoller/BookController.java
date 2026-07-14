package com.contoller;

import com.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Scanner;

@Controller
public class BookController {

    @Autowired
    BookService bookService;

    public void startMenu(){
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        while(isRunning){
            System.out.print("""
                    === Menu ===:\s
                    1. Add record\s
                    2. List records\s
                    3. Find record\s
                    4. Exit\s
                    """);
            String input = scanner.nextLine();
            switch (input.trim()){
                case "1" -> System.out.println("One");
                case "2" -> getAllRecords();
                case "4" -> isRunning = false;
                default -> System.out.println("Unknown command. Try again.");
            }
        }
    }

    private void getAllRecords(){
        List<String> records = bookService.getAllRecords();
        if (records.isEmpty()) {
            System.out.println("List is empty");
        } else {
            records.forEach(System.out::println);
        }
    }
}
