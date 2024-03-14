package com.soon.cboard.controller;

import com.soon.cboard.entity.Cboard;
import com.soon.cboard.service.CboardService;
import com.soon.jwt.TokenProvider;
import com.soon.jwt.TokenUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/cboard")
public class CboardController {

    @Autowired
    private CboardService cboardService;

    @Autowired
    private TokenProvider tokenProvider;


    @GetMapping("/boards")
    public Page<Cboard> getBoardsByPage(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int pageSize) {

        log.info("- page : {}", page);

        PageRequest pageRequest = PageRequest.of(page, pageSize);

        return cboardService.getBoardsByPage(pageRequest);
    }

    @GetMapping("/board/{id}")
    public Cboard getBoardById(@PathVariable Long id) {
        return cboardService.getBoardById(id);
    }

    @PostMapping("/board")
    public Cboard createBoard(@RequestPart("board") Cboard board,
                              @RequestPart("file") MultipartFile file,
                              @RequestHeader("Authorization") String token) {
        // 파일 처리 로직 추가
        return cboardService.createBoard(board, file, token);
    }

    @PutMapping("/board/{id}")
    public Cboard updateBoard(@PathVariable Long id, @RequestBody Cboard updatedBoard) {
        return cboardService.updateBoard(id, updatedBoard);
    }

    @DeleteMapping("/board/{id}")
    public void deleteBoard(@PathVariable Long id) {
        cboardService.deleteBoard(id);
    }

    @GetMapping("/board/check-permission/{id}")
    public ResponseEntity<Map<String, Boolean>> checkPermission(@PathVariable Long id, @RequestHeader("Authorization") String token) {


        // 토큰에서 사용자 정보를 추출
        TokenUserInfo userInfo = tokenProvider.validateAndReturnTokenUserInfo(token.substring(7));

        // 게시물 작성자와 현재 사용자의 닉네임을 확인하여 권한 부여 여부 결정
        boolean hasPermission = cboardService.hasPermission(id, userInfo.getUserNick());

        Map<String, Boolean> response = new HashMap<>();
        response.put("hasPermission", hasPermission);

        return ResponseEntity.ok(response);
    }
}
