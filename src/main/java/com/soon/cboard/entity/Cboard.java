package com.soon.cboard.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
public class Cboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;
    private String title;
    private String content;

    private String fileUrl;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;


}
