package com.soon.cboard.repository;

import com.soon.cboard.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

//    List<Comment> findByBoardId(Long boardId);

    List<Comment> findByBoardIdOrderByCreatedAtDesc(Long boardId);

    void deleteByBoardId(Long boardId);
}
