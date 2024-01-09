package com.soon.user.service;

import com.soon.user.dto.KakaoTokenResponseDTO;
import com.soon.user.entity.KakaoUser;
import com.soon.user.repository.KakaoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoApiService {

    private final KakaoUserRepository kakaoUserRepository;

    @Autowired
    public KakaoApiService(KakaoUserRepository kakaoUserRepository) {
        this.kakaoUserRepository = kakaoUserRepository;
    }

    @Value("${kakao.api-url}")
    private String kakaoApiUrl;

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.client-secret}")
    private String kakaoClientSecret;

    public KakaoUser getKakaoUserInfo(String code) {
        KakaoTokenResponseDTO tokenResponse = getAccessToken(code);
        String token = tokenResponse.getAccess_token();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> response = new RestTemplate().exchange(
                kakaoApiUrl + "/v2/user/me",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        Map<String, Object> userInfoMap = response.getBody();
        KakaoUser kakaoUser;

        if (userInfoMap != null) {
            kakaoUser = convertToKakaoUser(userInfoMap);
            kakaoUserRepository.save(kakaoUser);
            // 여기에서 kakaoUser 활용 또는 반환 처리
        } else {
            throw new RuntimeException("에러 발생! 카카오 정보가 없습니다!");
        }

        return kakaoUser;
    }

    private KakaoTokenResponseDTO getAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("client_id", kakaoClientId);
        params.put("client_secret", kakaoClientSecret);
        params.put("redirect_uri", "YOUR_REDIRECT_URI");
        params.put("code", code);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoTokenResponseDTO> response = new RestTemplate().exchange(
                kakaoApiUrl + "/v1/token",
                HttpMethod.POST,
                entity,
                KakaoTokenResponseDTO.class
        );

        return response.getBody();
    }

    private KakaoUser convertToKakaoUser(Map<String, Object> userInfoMap) {
        // 여기에서 userInfoMap을 KakaoUser 객체로 변환하여 반환
        // 예를 들어, ID, 닉네임 등을 추출하여 KakaoUser 객체에 설정
        KakaoUser kakaoUser = new KakaoUser();
        kakaoUser.setKakaoId(userInfoMap.get("id").toString());
        kakaoUser.setNickname(userInfoMap.get("properties.nickname").toString());
        // 나머지 필드도 필요에 따라 설정

        return kakaoUser;
    }
}