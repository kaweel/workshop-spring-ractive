package com.org.kaweel.reactive.student;

import com.org.kaweel.reactive.config.handle.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    StudentService studentService;

    @Mock
    StudentRepository studentRepository;

    @BeforeEach
    private void setUp() {
        studentService = new StudentService(studentRepository);
    }

    @Test
    @DisplayName("find all student, not found should return msg 'NOT_FOUND'")
    public void findAll_not_found() {
        BDDMockito.given(studentRepository.findAll()).willReturn(Flux.empty());
        studentService.findAll()
                .as(StepVerifier::create)
                .expectNextCount(0)
                .expectErrorMatches(i -> {
                    CustomException s = (CustomException) i;
                    boolean matchCode = HttpStatus.NOT_FOUND.equals(s.getStatusCode());
                    boolean matchMsg = HttpStatus.NOT_FOUND.getReasonPhrase().equals(s.getResponseMsg().getMessage());
                    return matchCode && matchMsg;
                })
                .verify();
    }

    @Test
    @DisplayName("find all student, found total student should greater than '0'")
    public void findAll_found() {
        BDDMockito.given(studentRepository.findAll()).willReturn(Flux.just(new Student()));
        studentService.findAll()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("find by student id '0', not found should return msg 'NOT_FOUND'")
    public void findById_not_found() {
        BDDMockito.given(studentRepository.findById(Mockito.anyInt())).willReturn(Mono.empty());
        studentService.findById(0)
                .as(StepVerifier::create)
                .expectNextCount(0)
                .expectErrorMatches(i -> {
                    CustomException s = (CustomException) i;
                    boolean matchCode = HttpStatus.NOT_FOUND.equals(s.getStatusCode());
                    boolean matchMsg = HttpStatus.NOT_FOUND.getReasonPhrase().equals(s.getResponseMsg().getMessage());
                    return matchCode && matchMsg;
                })
                .verify();
    }

    @Test
    @DisplayName("find by student id '1', student name should be 'Ryoma'")
    public void findById_found() {

        Student student = new Student();
        student.setId(1);
        student.setName("Ryoma");
        student.setGrade(Grade.HIGH_SCHOOL.getValue());
        student.setPassword("");

        BDDMockito.given(studentRepository.findById(Mockito.anyInt())).willReturn(Mono.just(student));
        studentService.findById(1)
                .as(StepVerifier::create)
                .expectNextMatches(i -> i.getName().equals("Ryoma"))
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("find by student grade 'MIDDLE_SCHOOL', not found should return msg 'NOT_FOUND'")
    public void findByGrade_not_found() {
        BDDMockito.given(studentRepository.findByGrade(Mockito.anyString())).willReturn(Flux.empty());
        studentService.findByGrade(Grade.MIDDLE_SCHOOL)
                .as(StepVerifier::create)
                .expectNextCount(0)
                .expectErrorMatches(i -> {
                    CustomException s = (CustomException) i;
                    boolean matchCode = HttpStatus.NOT_FOUND.equals(s.getStatusCode());
                    boolean matchMsg = HttpStatus.NOT_FOUND.getReasonPhrase().equals(s.getResponseMsg().getMessage());
                    return matchCode && matchMsg;
                })
                .verify();
    }

    @Test
    @DisplayName("find by student grade 'HIGH_SCHOOL', found student name should be 'Ryoma'")
    public void findByGrade_found() {

        Student s1 = new Student();
        s1.setId(1);
        s1.setName("Ryoma");
        s1.setGrade(Grade.HIGH_SCHOOL.getValue());
        s1.setPassword("");

        Student s2 = new Student();
        s2.setId(2);
        s2.setName("Airi");
        s2.setGrade(Grade.HIGH_SCHOOL.getValue());
        s2.setPassword("");

        BDDMockito.given(studentRepository.findByGrade(Mockito.anyString())).willReturn(Flux.just(s1, s2));
        studentService.findByGrade(Grade.HIGH_SCHOOL)
                .as(StepVerifier::create)
                .expectNextMatches(i -> {
                    boolean matchName = i.getName().equals("Ryoma");
                    boolean matchGrade = Grade.HIGH_SCHOOL.equals(i.getGrade());
                    return matchName && matchGrade;
                })
                .expectNextMatches(i -> {
                    boolean matchName = i.getName().equals("Airi");
                    boolean matchGrade = Grade.HIGH_SCHOOL.equals(i.getGrade());
                    return matchName && matchGrade;
                })
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("create student name 'Simon', student name already exists should return msg 'CONFLICT'")
    public void create_duplicate_name() {

        Student s1 = new Student();
        s1.setName("Simon");
        s1.setGrade(Grade.HIGH_SCHOOL.getValue());
        s1.setPassword("");

        BDDMockito.given(studentRepository.findByName(Mockito.anyString())).willReturn(Mono.just(s1));
        BDDMockito.given(studentRepository.save(Mockito.any())).willReturn(Mono.empty());
        studentService.create(s1)
                .as(StepVerifier::create)
                .expectNextCount(0)
                .expectErrorMatches(i -> {
                    CustomException s = (CustomException) i;
                    boolean matchCode = HttpStatus.CONFLICT.equals(s.getStatusCode());
                    boolean matchMsg = HttpStatus.CONFLICT.getReasonPhrase().equals(s.getResponseMsg().getMessage());
                    return matchCode && matchMsg;
                })
                .verify();

        BDDMockito.verify(studentRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("create student name 'Escobar', student name not exists should return msg 'OK'")
    public void create() {

        Student s1 = new Student();
        s1.setName("Escobar");
        s1.setGrade(Grade.HIGH_SCHOOL.getValue());
        s1.setPassword("");

        BDDMockito.given(studentRepository.findByName(Mockito.anyString())).willReturn(Mono.empty());
        BDDMockito.given(studentRepository.save(Mockito.any())).willReturn(Mono.just(s1));
        studentService.create(s1)
                .as(StepVerifier::create)
                .expectNextMatches(i -> HttpStatus.OK.getReasonPhrase().equals(i.getMessage()))
                .expectComplete()
                .verify();
        BDDMockito.verify(studentRepository, Mockito.times(1)).save(Mockito.any());

    }

    @Test
    @DisplayName("update student data name 'Simon', student name not exists should return msg 'NOT_FOUND'")
    public void update_student_not_exists() {

        Student s1 = new Student();
        s1.setId(1);
        s1.setName("Simon");
        s1.setGrade(Grade.HIGH_SCHOOL.getValue());
        s1.setPassword("");

        BDDMockito.given(studentRepository.findById(Mockito.anyInt())).willReturn(Mono.empty());
        studentService.update(s1)
                .as(StepVerifier::create)
                .expectNextCount(0)
                .expectErrorMatches(i -> {
                    CustomException s = (CustomException) i;
                    boolean matchCode = HttpStatus.NOT_FOUND.equals(s.getStatusCode());
                    boolean matchMsg = HttpStatus.NOT_FOUND.getReasonPhrase().equals(s.getResponseMsg().getMessage());
                    return matchCode && matchMsg;
                })
                .verify();
        BDDMockito.verify(studentRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    @DisplayName("update student data name 'Anne', student name exists should return msg 'OK'")
    public void update_student_exists() {

        Student s1 = new Student();
        s1.setId(1);
        s1.setName("Anne");
        s1.setGrade(Grade.HIGH_SCHOOL.getValue());
        s1.setPassword("");

        BDDMockito.given(studentRepository.findById(Mockito.anyInt())).willReturn(Mono.just(s1));
        BDDMockito.given(studentRepository.save(Mockito.any())).willReturn(Mono.just(s1));
        studentService.update(s1)
                .as(StepVerifier::create)
                .expectNextMatches(i -> HttpStatus.OK.getReasonPhrase().equals(i.getMessage()))
                .expectComplete()
                .verify();

        BDDMockito.verify(studentRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("delete student id '0', student id not exists should return msg 'NOT_FOUND'")
    public void delete_student_not_exists() {

        BDDMockito.given(studentRepository.findById(Mockito.anyInt())).willReturn(Mono.empty());
        BDDMockito.given(studentRepository.deleteById(Mockito.anyInt())).willReturn(Mono.when());
        studentService.delete(0)
                .as(StepVerifier::create)
                .expectErrorMatches(i -> {
                    CustomException s = (CustomException) i;
                    boolean matchCode = HttpStatus.NOT_FOUND.equals(s.getStatusCode());
                    boolean matchMsg = HttpStatus.NOT_FOUND.getReasonPhrase().equals(s.getResponseMsg().getMessage());
                    return matchCode && matchMsg;
                })
                .verify();

        BDDMockito.verify(studentRepository, Mockito.times(1)).deleteById(Mockito.anyInt());
    }

    @Test
    @DisplayName("delete student id '1', student id not exists should return msg 'NOT_FOUND'")
    public void delete_student_exists() {

        Student s1 = new Student();
        s1.setId(1);
        s1.setName("Anne");
        s1.setGrade(Grade.HIGH_SCHOOL.getValue());
        s1.setPassword("");

        BDDMockito.given(studentRepository.findById(Mockito.anyInt())).willReturn(Mono.just(s1));
        BDDMockito.given(studentRepository.deleteById(Mockito.anyInt())).willReturn(Mono.when());
        studentService.delete(1)
                .as(StepVerifier::create)
                .expectComplete()
                .verify();

        BDDMockito.verify(studentRepository, Mockito.times(1)).deleteById(Mockito.anyInt());
    }

}
