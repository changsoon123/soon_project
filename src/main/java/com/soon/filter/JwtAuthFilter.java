package com.soon.filter;

import com.soon.jwt.TokenProvider;
import com.soon.jwt.TokenUserInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    // 필터 처리할 내용
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = parseBearerToken(request);
            log.info("Jwt Token Filter is running... - token : {}", token);

            // 토큰 위조 검사 및 인증 완료 처리
            if(token != null) {

                // 토큰 서명 위조 검사와 토큰을 파싱해서 클레임을 얻어내는 작업
                TokenUserInfo userInfo = tokenProvider.validateAndReturnTokenUserInfo(token);

                // 인가 정보 리스트
                // 회원 권한 기능 추가 시 주석 해제 후 설정
//                List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
//                authorityList.add(new SimpleGrantedAuthority("ROLE_" + userInfo.getRole().toString()));

                //인증 완료 처리
                // - 스프링 시큐리티에게 인증 정보를 전달해서 전역적으로 앱에서 인증 정보를 활용할 수 있게 설정
                AbstractAuthenticationToken auth
                        = new UsernamePasswordAuthenticationToken(
                        userInfo, // 컨트롤러에서 활용할 유저 정보
                        null, // 인증된 사용자의 비밀번호 - 보통 널값
                        null// 회원 권한 기능 추가 시 주석 해제 후 설정
//                        ,authorityList // 인가 정보 (권한 정보)
                );

                // 인증 완료 처리시 클라이언트의 요청 정보 세팅
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 스프링 시큐리티 컨테이너에 인증 정보 객체 등록
                SecurityContextHolder.getContext().setAuthentication(auth);

            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("토큰이 위조 되었거나 만료되었습니다.");
        }

        // 필터 체인에 내가 만든 필터 실행 명령
        filterChain.doFilter(request, response);

    }

    private String parseBearerToken(HttpServletRequest request) {

        // 요청 헤더에서 토큰 가져오기
        // http request header
        // -- Content-type : application/json
        // -- Authorization : Bearer asjkaldksfjsf$%aaorfsaefjasdf
        String  bearerToken = request.getHeader("Authorization");
        log.info("Jwt Token Filter is running... - bearerToken : {}", bearerToken);

        // 요청 헤더에서 가져온 토큰은 순수 토큰값이 아닌 앞에 Bearer가 붙어있으니 이것을 제거하는 작업
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}

