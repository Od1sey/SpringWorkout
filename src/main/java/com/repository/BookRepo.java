package com.repository;

import com.entity.Book;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {

    List<Book> findByAuthorId(int id);

    @Query("""
            select b
            from Book b
            join fetch b.author
            where lower(b.name)
            like lower(concat('%', :text, '%'))
            """)
    List<Book> findByNameContaining(@Param("text") String text);

    @Query("""
            select b
            from Book b
            join fetch b.author
            """)
    List<Book> findAllWithAuthors();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select b
            from Book b
            where b.id = :id
            """)
    Optional<Book> findByIdForUpdate(@Param("id") Integer id);

}
