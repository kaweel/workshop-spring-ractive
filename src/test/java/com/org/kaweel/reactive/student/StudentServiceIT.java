package com.org.kaweel.reactive.student;


import com.org.kaweel.reactive.ReactiveApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ReactiveApplication.class)
@ActiveProfiles("h2")
@Tag("integration")
public class StudentServiceIT {

    @Autowired
    StudentService studentService;

    @BeforeEach
    public void setUp() {
        Student s1 = new Student(null, "Mon", Grade.HIGH_SCHOOL.getValue(), "p2");
        studentService.create(s1).subscribe();
    }

    @AfterEach
    public void cleanUp() {
        studentService.deleteAll().subscribe();
    }

    @Test
    public void getAll() {
        studentService.findAll()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void delete() {
        studentService.delete(1)
                .as(StepVerifier::create)
                .verifyComplete();
    }
}
