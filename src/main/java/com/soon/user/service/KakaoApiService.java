package com.soon.user.service;

import com.soon.jwt.TokenProvider;
import com.soon.user.entity.KakaoUser;
import com.soon.user.repository.KakaoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KakaoApiService {
    @Autowired
    private KakaoUserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    public KakaoUser findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    public String save(KakaoUser user) {

        userRepository.save(user);

        return tokenProvider.createKakaoToken(user);


    }
}