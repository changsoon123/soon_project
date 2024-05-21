package com.soon.cboard.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class BoardTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Cboard board;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;
}
