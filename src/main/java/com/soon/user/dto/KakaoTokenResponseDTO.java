package com.soon.user.dto;

import lombok.Data;

@Data
public class KakaoTokenResponseDTO {

    private String access_token;
    private String token_type;
    private String refresh_token;
    private long expires_in;
    private String scope;

    // 생성자, getter 등 필요한 메서드 구현
}
