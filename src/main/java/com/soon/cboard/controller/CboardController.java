package com.soon.cboard.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soon.cboard.entity.BoardTag;
import com.soon.cboard.entity.Cboard;
import com.soon.cboard.entity.Tag;
import com.soon.cboard.repository.TagRepository;
import com.soon.cboard.service.CboardService;
import com.soon.cboard.service.FileUploadService;
import com.soon.jwt.TokenProvider;
import com.soon.jwt.TokenUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
    public Cboard createBoard(@RequestPart("title") String title,
                              @RequestPart("content") String content,
                              @RequestPart(value="tags", required = false) String tags,
                              @RequestPart(value = "file", required = false) List<MultipartFile> files,
                              @RequestHeader("Authorization") String token) {

        try {

            System.out.println(tags);


            Cboard board = new Cboard();
            board.setTitle(title);
            board.setContent(content);
//            board.setBoardTags(boardTags);
//            System.out.println(files);

            // 여러 개의 파일을 처리하는 로직
            if (files != null && !files.isEmpty()) {
                List<String> fileUrls = fileUploadService.uploadFiles(files);
                board.setFileUrls(fileUrls);
            }

//            if (tags != null) {
//                List<BoardTag> boardTags = new ArrayList<>();
//                for (String tagName : tags) {
//                    Tag tag = tagRepository.findByName(tagName);
//                    if (tag == null) {
//                        tag = new Tag();
//                        tag.setName(tagName);
//                        tag = tagRepository.save(tag);
//                    }
//                    BoardTag boardTag = new BoardTag();
//                    boardTag.setTag(tag);
//                    boardTag.setBoard(board);  // 보드와의 관계 설정
//                    boardTags.add(boardTag);
//                }
//                board.setBoardTags(boardTags);
//            }

            return cboardService.createBoard(board, token);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PutMapping("/board/{id}")
    public Cboard updateBoard(
                                @PathVariable Long id,
                                @RequestPart("title") String title,
                                @RequestPart("content") String content,
                                @RequestPart(value = "file", required = false) List<MultipartFile> files
                                ) {
        try {
            Cboard board = new Cboard();
            board.setTitle(title);
            board.setContent(content);

//            System.out.println(files);

            // 여러 개의 파일을 처리하는 로직
            if (files != null && !files.isEmpty()) {
                List<String> fileUrls = fileUploadService.uploadFiles(files);
                board.setFileUrls(fileUrls);
            }

            return cboardService.updateBoard(id, board);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @DeleteMapping("/board/{id}")
    public void deleteBoard(@PathVariable Long id
                            ) {
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
