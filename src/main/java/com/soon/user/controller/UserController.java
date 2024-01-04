package com.soon.user.controller;

import com.soon.user.dto.LoginRequestDTO;
import com.soon.user.dto.LoginResponseDTO;
import com.soon.user.dto.UserRequestDTO;
import com.soon.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserRequestDTO userRequest) {
        userService.signUp(userRequest);
        return ResponseEntity.ok("회원 가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        // 로그인 로직 처리
        // 로그인 성공 시 토큰 생성 및 반환

        String token = userService.login(loginRequest);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

}
