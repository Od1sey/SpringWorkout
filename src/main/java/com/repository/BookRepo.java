package com.repository;

import com.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class BookRepo {

    private final  JdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createRecord(String name){
        jdbcTemplate.update("INSERT INTO books (name, added_at) VALUES (?, ?)", name, LocalDateTime.now());
    }

    public List<Book> getAllRecords(){
        return jdbcTemplate.query("SELECT * FROM books", new BeanPropertyRowMapper<>(Book.class));
    }

    public List<Book> getRecordsByQuery(String query){
        return jdbcTemplate.query("SELECT * FROM books WHERE name like ?",
                new BeanPropertyRowMapper<>(Book.class), "%"+query+"%");
    }

}
