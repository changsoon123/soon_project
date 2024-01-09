package com.soon.user.controller;


import com.soon.jwt.TokenProvider;
import com.soon.user.entity.KakaoUser;
import com.soon.user.service.KakaoApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class KakaoLoginController {


    @Autowired
    private TokenProvider tokenProvider;

    private final KakaoApiService kakaoApiService;

    @Autowired
    public KakaoLoginController(KakaoApiService kakaoApiService) {
        this.kakaoApiService = kakaoApiService;
    }

    @CrossOrigin
    @GetMapping("/login/kakao/callback")
    public ResponseEntity<String> handleKakaoCallback(@RequestParam("code") String code) {
        try {
            // 카카오로부터 받은 code를 사용하여 사용자 정보 요청
            KakaoUser userInfo = kakaoApiService.getKakaoUserInfo(code);
            String jwtToken = tokenProvider.createKakaoToken(userInfo);
            return ResponseEntity.ok(jwtToken);
        } catch (Exception e) {
            throw new RuntimeException("로그인에 실패했습니다. 원인: " + e.getMessage());
        }

   }




}
