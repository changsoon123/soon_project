package com.soon.user.service;

import com.soon.user.entity.KakaoUser;
import com.soon.user.repository.KakaoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KakaoApiService {
    @Autowired
    private KakaoUserRepository userRepository;

    public KakaoUser findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    public KakaoUser save(KakaoUser user) {
        return userRepository.save(user);
    }
}