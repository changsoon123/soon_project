package com.soon.cboard.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Cboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;
    private String title;
    private String content;

    @Column(nullable = true)
    private String fileUrl;

    @ElementCollection
    @CollectionTable(name = "cboard_file_urls", joinColumns = @JoinColumn(name = "cboard_id"))
    @Column(name = "file_url")
    private List<String> fileUrls;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;


}
