package com.porraglobal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Test de integración: levanta un MySQL real con Testcontainers
 * y verifica que el contexto de Spring arranca y el esquema valida.
 * Requiere Docker en ejecución.
 */
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
class PorraGlobalApplicationIT {

    @Container
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.4")
            .withDatabaseName("porra_global")
            .withUsername("porra")
            .withPassword("porra_secret")
            .withInitScript("db/01_schema.sql");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Test
    void contextLoads() {
        // Si el contexto arranca y Hibernate valida el esquema, el test pasa.
    }
}
