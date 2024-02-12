package com.soon.user.controller;


import com.soon.user.dto.LoginResponseDTO;
import com.soon.user.entity.KakaoUser;
import com.soon.user.service.KakaoApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kakao")
public class KakaoLoginController {


    @Autowired
    private KakaoApiService kakaoService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody KakaoUser user) {
        // 로그인 로직 처리
        String token = kakaoService.save(user);
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

}
