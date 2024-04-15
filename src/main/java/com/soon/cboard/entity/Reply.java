package com.soon.cboard.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long boardId;

    @Column(nullable = false)
    private Long parentCommentId;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

}
