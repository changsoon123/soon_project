package com.soon.user.controller;


import com.soon.user.entity.KakaoUser;
import com.soon.user.service.KakaoApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class KakaoLoginController {


    @Autowired
    private KakaoApiService kakaoService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody KakaoUser user) {
        // 로그인 로직 처리
        KakaoUser existingUser = kakaoService.findByUserId(user.getKakaoId());
        if (existingUser == null) {
            // 새로운 사용자, 데이터베이스에 저장
            kakaoService.save(user);
            return ResponseEntity.ok("새로운 사용자로 등록되었습니다.");
        } else {
            // 기존 사용자, 필요한 작업 수행
            return ResponseEntity.ok("기존 사용자입니다. 필요한 작업을 수행하세요.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody KakaoUser user) {
        // 로그아웃 로직 처리
        KakaoUser existingUser = kakaoService.findByUserId(user.getKakaoId());
        if (existingUser != null) {
            // 로그아웃 시 필요한 작업 수행
            kakaoService.logout(existingUser);
            return ResponseEntity.ok("로그아웃되었습니다.");
        } else {
            return ResponseEntity.status(400).body("유효하지 않은 사용자입니다.");
        }
    }

}
