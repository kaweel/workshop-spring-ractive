package com.org.kaweel.reactive.student;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentRepository extends ReactiveCrudRepository<Student, Integer> {

    Flux<Student> findByGrade(String grade);

    Mono<Student> findByName(String name);

}
