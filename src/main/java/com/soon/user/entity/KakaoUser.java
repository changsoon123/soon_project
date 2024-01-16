package com.soon.user.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class KakaoUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "nickname")
    private String nickname;

}
