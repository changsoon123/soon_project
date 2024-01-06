package com.soon.user.dto;


import lombok.Data;

@Data
public class UserRequestDTO {

    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String nickname;

}
