package com.soon.cboard.controller;

import com.soon.cboard.entity.Comment;
import com.soon.cboard.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


}