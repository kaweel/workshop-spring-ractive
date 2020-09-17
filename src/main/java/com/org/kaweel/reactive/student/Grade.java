package com.org.kaweel.reactive.student;

import lombok.Getter;

@Getter
public enum Grade {
    ELEMENTARY_SCHOOL("1"),
    MIDDLE_SCHOOL("2"),
    HIGH_SCHOOL("3");

    private String value;

    Grade(String value) {
        this.value = value;
    }

    public boolean equals(String name) {
        return value.equals(name);
    }
}
