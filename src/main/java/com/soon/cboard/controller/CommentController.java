package com.soon.cboard.controller;

import com.soon.cboard.entity.Comment;
import com.soon.cboard.service.CommentService;
import com.soon.jwt.TokenUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{boardId}")
    public List<Comment> getAllCommentsByBoardId(@PathVariable Long boardId) {
        return commentService.getAllCommentsByBoardId(boardId);
    }

    @PostMapping("/add/{boardId}")
    public Comment createComment(@RequestBody Comment comment,
                                 @PathVariable Long boardId,
                                 @RequestHeader("Authorization") String token) {
        // 댓글에 게시물 ID 정보 설정
        comment.setBoardId(boardId);
        return commentService.addComment(comment,token);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        if (commentService.deleteComment(commentId)) {
            return ResponseEntity.ok("댓글이 삭제되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("댓글 삭제에 실패했습니다.");
        }
    }

//    @GetMapping("/board/check-permission/{id}")
//    public ResponseEntity<Map<String, Boolean>> checkPermission(@PathVariable Long id, @RequestHeader("Authorization") String token) {
//
//
//        // 토큰에서 사용자 정보를 추출
//        TokenUserInfo userInfo = tokenProvider.validateAndReturnTokenUserInfo(token.substring(7));
//
//        // 게시물 작성자와 현재 사용자의 닉네임을 확인하여 권한 부여 여부 결정
//        boolean hasPermission = cboardService.hasPermission(id, userInfo.getUserNick());
//
//        Map<String, Boolean> response = new HashMap<>();
//        response.put("hasPermission", hasPermission);
//
//        return ResponseEntity.ok(response);
//    }


}