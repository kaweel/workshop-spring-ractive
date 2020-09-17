package com.org.kaweel.reactive.student;

import com.org.kaweel.reactive.entity.ActionEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class Student extends ActionEntity {
    @Id
    private Integer id;
    @Column
    private String name;
    @Column
    private String grade;
    @Column
    private String password;

    public Student(String createBy, LocalDateTime createDate, String updateBy, LocalDateTime updateDate, Integer id, String name, String grade, String password) {
        super(createBy, createDate, updateBy, updateDate);
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.password = password;
    }
}
