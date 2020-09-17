package com.org.kaweel.reactive.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataR2dbcTest
public class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    DatabaseClient databaseClient;

    @BeforeEach
    public void setUp() {

        List<String> statements = Arrays.asList(
                "DROP TABLE IF EXISTS customer;",
                "CREATE TABLE IF NOT EXISTS student ( " +
                        "id SERIAL PRIMARY KEY, " +
                        "name VARCHAR(30) NOT NULL, " +
                        "grade VARCHAR(1) NOT NULL, " +
                        "password VARCHAR(255) NOT NULL, " +
                        "create_by VARCHAR(30), " +
                        "create_date DATETIME, " +
                        "update_by VARCHAR(30), " +
                        "update_date DATETIME" +
                        ");"
        );

        statements.forEach(it -> databaseClient.execute(it)
                .fetch()
                .rowsUpdated()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete());
    }

    @AfterEach
    public void cleanUp() {
        studentRepository.deleteAll().subscribe();
    }

    @Test
    @DisplayName("find all student not found total student should '0'")
    public void findAll_not_found() {
        studentRepository.findAll()
                .as(StepVerifier::create)
                .expectNextCount(0)
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("find all student not found total student should '2'")
    public void findAll_found() {
        this.insert();
        studentRepository.findAll()
                .log()
                .as(StepVerifier::create)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    private void insert() {
        List<Student> students = Arrays.asList(
                new Student("bo", LocalDateTime.now(), null, null, null, "Veera", Grade.ELEMENTARY_SCHOOL.getValue(), "p1"),
                new Student("bo", LocalDateTime.now(), null, null, null, "Raz", Grade.HIGH_SCHOOL.getValue(), "p2")
        );
        studentRepository.saveAll(students).subscribe();
    }
}
