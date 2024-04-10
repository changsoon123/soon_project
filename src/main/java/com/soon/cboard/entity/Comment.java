package com.soon.cboard.entity;

// Comment 모델 클래스

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String author;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    private Long boardId;
}
