package integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

@Testcontainers
public abstract class IntegrationTestBase {

    @Container
    static PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:16");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("test.datasource.url", postgres::getJdbcUrl);
        registry.add("test.datasource.username", postgres::getUsername);
        registry.add("test.datasource.password", postgres::getPassword);
    }
}