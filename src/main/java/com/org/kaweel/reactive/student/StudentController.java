package com.org.kaweel.reactive.student;

import com.org.kaweel.reactive.config.handle.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/student")
public class StudentController {

    private StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("")
    public Flux<Student> findAll() {
        return studentService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Student> findById(@PathVariable Integer id) {
        return studentService.findById(id);
    }

    @GetMapping("/")
    public Flux<Student> findByGrade(@RequestParam Grade grade) {
        return studentService.findByGrade(grade);
    }

    @PostMapping("")
    public Mono<CustomException.ResponseMsg> create(@RequestBody Student student) {
        return studentService.create(student);
    }

    @PutMapping("")
    public Mono<CustomException.ResponseMsg> update(@RequestBody Student student) {
        return studentService.update(student);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Integer id) {
        return studentService.delete(id);
    }


}
