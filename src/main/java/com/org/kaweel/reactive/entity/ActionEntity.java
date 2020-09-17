package com.org.kaweel.reactive.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ActionEntity {
    @Column(value = "create_by")
    private String createBy;
    @Column(value = "create_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Bangkok")
    private LocalDateTime createDate;
    @Column(value = "update_by")
    private String updateBy;
    @Column(value = "update_date")
    private LocalDateTime updateDate;
}
