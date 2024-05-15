package com.soon.cboard.service;

import com.soon.cboard.entity.Cboard;
import com.soon.cboard.entity.Comment;
import com.soon.cboard.repository.CommentRepository;
import com.soon.jwt.TokenProvider;
import com.soon.jwt.TokenUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        return commentRepository.findByBoardIdOrderByCreatedAtDesc(boardId);
    }

    public Comment addComment(Comment comment, String token) {

        TokenUserInfo userInfo = tokenProvider.validateAndReturnTokenUserInfo(token.substring(7));
        comment.setAuthor(userInfo.getUserNick());

        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public boolean deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment != null) {
            commentRepository.delete(comment);
            return true;
        } else {
            return false;
        }
    }

    public Comment getCommentById(Long id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        return commentOptional.orElse(null);
    }


    public boolean hasPermission(Long id, String userNick) {

        Optional<Comment> optionalComment = commentRepository.findById(id);

        System.out.println(optionalComment);

        if (optionalComment.isEmpty()) {
            return false;
        }

        Comment comment = optionalComment.get();
        return comment.getAuthor().equals(userNick);
    }
}
