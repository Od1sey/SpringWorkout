package integration.service;

import com.repository.AuthorRepo;
import com.repository.BookRepo;
import com.service.AuthorService;
import com.service.BookService;
import com.service.LibraryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class LibraryServiceTestConfig {

    @Bean
    DataSource dataSource(
            @Value("${test.datasource.url}") String url,
            @Value("${test.datasource.username}") String username,
            @Value("${test.datasource.password}") String password
    ) {
        DriverManagerDataSource dataSource =
                new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(initMethod = "initiateDatabase")
    AuthorRepo authorRepo(JdbcTemplate jdbcTemplate) {
        return new AuthorRepo(jdbcTemplate);
    }

    @Bean(initMethod = "initiateDatabase")
    @DependsOn("authorRepo")
    BookRepo bookRepo(JdbcTemplate jdbcTemplate) {
        return new BookRepo(jdbcTemplate);
    }

    @Bean
    AuthorService authorService(AuthorRepo authorRepo) {
        return new AuthorService(authorRepo);
    }

    @Bean
    BookService bookService(BookRepo bookRepo) {
        return new BookService(bookRepo);
    }

    @Bean
    LibraryService libraryService(AuthorService authorService, BookService bookService) {
        return new LibraryService(authorService, bookService);
    }

    @Bean
    PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}