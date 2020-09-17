package com.org.kaweel.reactive.config.r2dbc;

import com.org.kaweel.reactive.student.Grade;
import com.org.kaweel.reactive.student.Student;
import com.org.kaweel.reactive.student.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;


@Slf4j
@Service
@Profile("h2")
public class InitialH2 {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    DatabaseClient databaseClient;

    @PostConstruct
    public void initialDb() {
        log.info("========== initial h2 database ==========");
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
                .subscribe());

//        List<Student> students = Arrays.asList(
//                new Student(null, "Veera", Grade.ELEMENTARY_SCHOOL.getValue(), "p1"),
//                new Student(null, "Raz", Grade.HIGH_SCHOOL.getValue(), "p2")
//        );
//        studentRepository.saveAll(students).subscribe();
    }
}
