package com.controller;

import com.entity.Book;
import com.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

@Controller
public class BookController {

    private final BookService bookService;
    private final Scanner scanner = new Scanner(System.in);
    private boolean isRunning = false;

    @Autowired
    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    public void startMainMenu(){
        isRunning = true;
        while(isRunning){
            System.out.print("""
                    
                    === Menu ===\s
                    Type "add" to add a record\s
                    Type "list" to list all records\s
                    Type "find" to find a record\s
                    Type "exit" to exit the program\s
                    ===========
                    
                    """);
            String input = scanner.nextLine().trim();
            switch (input){
                case "add" -> startNewRecordMenu();
                case "list" -> printAllRecords();
                case "find" -> startFindRecordMenu();
                case "exit" -> isRunning = false;
                default -> System.out.println("Unknown command. Try again.");
            }
        }
    }


    private void startNewRecordMenu(){
        boolean isAddingNewRecord = true;
        while(isAddingNewRecord){
            System.out.print("""
                    
                    === Adding new record ===\s
                    Please enter name of the book below
                    or "return" to exit this menu
                    
                    =========================
                    """);
            String input = scanner.nextLine().trim();
            if ("return".equals(input)){
               isAddingNewRecord = false;
            } else {
                try {
                    bookService.createNewRecord(input);
                    System.out.printf("Book with name %s was added", input);
                } catch (IllegalArgumentException ex) {
                    System.out.println("\n" + ex.getMessage()+ "\n");
                }
            }
        }
    }

    private void printAllRecords(){
        displayList(bookService.getAllRecords());
    }

    private void startFindRecordMenu(){
        boolean isFindingRecord = true;
        while (isFindingRecord){
            System.out.print("""
                    
                    === Lookup for a record ===\s
                    Please enter name of the book below
                    or "return" to exit this menu
                    ===========================

                    """);
            String input = scanner.nextLine().trim();
            if ("return".equals(input)){
                isFindingRecord = false;
            } else {
                try{
                    displayList(bookService.getRecordsByQuery(input));
                } catch (IllegalArgumentException ex){
                    System.out.println("\n" + ex.getMessage()+ "\n");
                }
            }
        }
    }

    private void displayList(List<Book> books){
        if (books.isEmpty()) {
            System.out.println("List is empty");
        } else {
            System.out.println();
            DateTimeFormatter format =  DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            books.forEach(book -> System.out.println(book.getName() + " " + book.getAddedAt().format(format)));
        }
    }
}
