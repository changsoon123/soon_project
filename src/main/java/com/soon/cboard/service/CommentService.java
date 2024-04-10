package com.soon.cboard.service;

import com.soon.cboard.entity.Comment;
import com.soon.cboard.repository.CommentRepository;
import com.soon.jwt.TokenProvider;
import com.soon.jwt.TokenUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getAllCommentsByBoardId(Long boardId) {
        return commentRepository.findByBoardId(boardId);
    }

    public Comment addComment(Comment comment, String token) {

        TokenUserInfo userInfo = tokenProvider.validateAndReturnTokenUserInfo(token.substring(7));
        comment.setAuthor(userInfo.getUserNick());

        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }
}
