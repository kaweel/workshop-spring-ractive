package com.org.kaweel.reactive.student;

import com.org.kaweel.reactive.config.handle.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class StudentService {

    private StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    private Student updateStudent(Student source, Student target) {
        BeanUtils.copyProperties(source, target);
        return target;
    }

    private static CustomException.ResponseMsg OK(Object i) {
        return new CustomException.ResponseMsg(HttpStatus.OK);
    }

    private static Student transform(Student i) {
        return i;
    }

    private static boolean highSchool(Student item) {
        return Grade.HIGH_SCHOOL.equals(item.getGrade());
    }

    public Flux<Student> findAll() {
        return studentRepository.findAll()
                .log()
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND)))
                .map(StudentService::transform);
    }

    public Mono<Student> findById(Integer id) {
        return studentRepository.findById(id)
                .log()
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND)))
                .map(StudentService::transform);
    }

    public Flux<Student> findByGrade(Grade grade) {
        return studentRepository.findByGrade(grade.getValue())
                .log()
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND)))
                .map(StudentService::transform);
    }

    @Transactional
    public Mono<CustomException.ResponseMsg> create(Student student) {
        return Mono.just(student)
                .log()
                .flatMap(i -> studentRepository.findByName(student.getName()))
                .flatMap(i -> Mono.error(new CustomException(HttpStatus.CONFLICT)))
                .switchIfEmpty(studentRepository.save(student))
                .map(StudentService::OK);
    }

    @Transactional
    public Mono<CustomException.ResponseMsg> update(Student student) {
        return Mono.just(student)
                .log()
                .flatMap(i -> studentRepository.findById(student.getId()))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND)))
                .map(i -> this.updateStudent(student, i))
                .flatMap(i -> studentRepository.save(i))
                .map(StudentService::OK);
    }

    @Transactional
    public Mono<Void> delete(Integer id) {
        return Mono.just(id)
                .flatMap(i -> studentRepository.findById(id))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND)))
                .then(studentRepository.deleteById(id));
    }

    @Transactional
    public Mono<Void> deleteAll() {
        return studentRepository.deleteAll();
    }

}
