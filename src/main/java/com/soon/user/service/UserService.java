package com.soon.user.service;

import com.soon.jwt.TokenProvider;
import com.soon.user.dto.LoginRequestDTO;
import com.soon.user.dto.UserRequestDTO;
import com.soon.user.entity.User;
import com.soon.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isFieldAvailable(String fieldName, String value) {

        return switch (fieldName) {
            case "username" -> isUsernameUnique(value);
            case "email" -> isEmailUnique(value);
            case "phoneNumber" -> isPhoneNumberUnique(value);
            case "nickname" -> isNickUnique(value);
            // 다른 필드에 대한 중복 체크도 추가할 수 있습니다.
            default -> throw new IllegalArgumentException("Unsupported field name: " + fieldName);
        };
    }

    // 회원 가입 메서드
    public void signUp(UserRequestDTO userRequest) {

        // 데이터 일관성 검증
        if (!validateUserData(userRequest)) {
            throw new IllegalArgumentException("입력된 데이터가 올바르지 않습니다.");
        }

        if (!isValidUsername(userRequest.getUsername())) {
            throw new IllegalArgumentException("입력된 아이디가 올바르지 않습니다.");
        }

        if(!isUsernameUnique(userRequest.getUsername())){
            throw new IllegalArgumentException("중복된 아이디입니다.");
        }

        // 비밀번호 강도 체크
        if (!isPasswordStrong(userRequest.getPassword())) {
            throw new IllegalArgumentException("입력된 비밀번호가 잘못 되었습니다.");
        }

        // 이메일 유효성 검사
        if (!isValidEmail(userRequest.getEmail())) {
            throw new IllegalArgumentException("유효하지 않은 이메일 주소입니다.");
        }

        if(!isEmailUnique(userRequest.getEmail())){
            throw new IllegalArgumentException("중복된 이메일 주소입니다.");
        }

        // 전화번호 형식 확인
        if (!isValidPhoneNumber(userRequest.getPhoneNumber())) {
            throw new IllegalArgumentException("유효하지 않은 전화번호 형식입니다.");
        }

        if(!isPhoneNumberUnique(userRequest.getPhoneNumber())){
            throw new IllegalArgumentException("중복된 핸드폰 번호입니다.");
        }

        if (!isValidNickname(userRequest.getNickname())) {
            throw new IllegalArgumentException("유효하지 않은 닉네임 형식입니다.");
        }

        if(!isNickUnique(userRequest.getNickname())){
            throw new IllegalArgumentException("중복된 닉네임 입니다.");
        }

        User newUser = new User();
        newUser.setUsername(userRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        newUser.setEmail(userRequest.getEmail());
        newUser.setPhoneNumber(userRequest.getPhoneNumber());
        newUser.setNickname(userRequest.getNickname());

        userRepository.save(newUser);
    }

    public String login(LoginRequestDTO loginRequest) {
        // 아이디로 사용자 찾기
        Optional<User> optionalUser = userRepository.findByUsername(loginRequest.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            System.out.println(user);
            // 비밀번호 검증
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                // 토큰 생성 및 반환
                return tokenProvider.createToken(user);
            }
        }

        // 사용자를 찾지 못하거나 비밀번호가 일치하지 않으면 예외 발생
        throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
    }

    // 데이터 일관성 검증
    private boolean validateUserData(UserRequestDTO userRequest) {
        return userRequest.getUsername() != null &&
                userRequest.getPassword() != null &&
                userRequest.getEmail() != null &&
                userRequest.getPhoneNumber() != null &&
                userRequest.getNickname() != null;
    }

    private boolean isValidUsername(String username) {
        // 최소 5자 이상, 영어 대소문자와 숫자 조합만 허용
        String usernameRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{5,}$";
        return username.matches(usernameRegex);
    }

    // 비밀번호 강도 체크
    private boolean isPasswordStrong(String password) {
        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        return password.matches(passwordRegex);
    }

    // 이메일 유효성 검사
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    // 전화번호 형식 확인
    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^\\d{3}-\\d{3,4}-\\d{4}$";
        return phoneNumber.matches(phoneRegex);
    }

    private boolean isValidNickname(String nickname) {
        String nicknameRegex = "^[A-Za-z0-9가-힣]{2,}$";
        return nickname.matches(nicknameRegex);
    }

    // 중복 검사 메서드 추가
    private boolean isUsernameUnique(String username) {
        return !userRepository.existsByUsername(username);
    }

    private boolean isEmailUnique(String email) {
        return !userRepository.existsByEmail(email);
    }

    private boolean isPhoneNumberUnique(String phoneNumber) {
        return !userRepository.existsByPhoneNumber(phoneNumber);
    }

    private boolean isNickUnique(String nick) {
        return !userRepository.existsByNickname(nick);
    }
}
