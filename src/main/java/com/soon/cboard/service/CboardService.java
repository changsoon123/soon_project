package com.soon.cboard.service;

import com.soon.cboard.entity.Cboard;
import com.soon.cboard.entity.Tag;
import com.soon.cboard.repository.CboardRepository;
import com.soon.cboard.repository.CommentRepository;
import com.soon.cboard.repository.TagRepository;
import com.soon.jwt.TokenProvider;
import com.soon.jwt.TokenUserInfo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CboardService {

    @Autowired
    private CboardRepository cboardRepository;

    @Autowired
    private CommentRepository commentRepository;


    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TokenProvider tokenProvider;


    public Page<Cboard> getBoardsByPage(Pageable pageable) {

        // JPA Repository를 사용하여 페이지별 게시물을 가져옴
        return cboardRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public Cboard getBoardById(Long id) {
        Optional<Cboard> boardOptional = cboardRepository.findById(id);
        return boardOptional.orElse(null);
    }


    public ResponseEntity<Cboard> createBoard(Cboard board, List<String> tagNames, String token) {

        TokenUserInfo userInfo = tokenProvider.validateAndReturnTokenUserInfo(token.substring(7));
        board.setNickname(userInfo.getUserNick());
        board.setCreatedAt(LocalDateTime.now());

        // 파일 URL을 포함하여 게시물을 저장합니다.
        Cboard savedBoard = cboardRepository.save(board);

        List<Tag> tags = new ArrayList<>();
        if (tagNames != null && !tagNames.isEmpty()) {
            for (String tagName : tagNames) {
                Optional<Tag> optionalTag = tagRepository.findByName(tagName);
                Tag tag;
                if (optionalTag.isPresent()) {
                    tag = optionalTag.get();
                } else {
                    tag = new Tag();
                    tag.setName(tagName);
                    tag.setCboardId(savedBoard.getId());
                    tag = tagRepository.save(tag);
                }

                tags.add(tag);
            }

        }

        savedBoard.setTags(tags);
        savedBoard = cboardRepository.save(savedBoard);
        // 저장된 게시물을 반환합니다.
        return ResponseEntity.status(HttpStatus.OK).body(savedBoard);
    }

    public Cboard updateBoard(Long id, Cboard board) {
        Optional<Cboard> existingBoardOptional = cboardRepository.findById(id);

        if (existingBoardOptional.isPresent()) {
            Cboard existingBoard = existingBoardOptional.get();
            existingBoard.setTitle(board.getTitle());
            existingBoard.setContent(board.getContent());
            existingBoard.setFileUrls(board.getFileUrls());
            return cboardRepository.save(existingBoard);
        } else {
            // 찾지 못했을 때 처리
            return null;
        }
    }

    @Transactional
    public void deleteBoard(Long id) {
        commentRepository.deleteByBoardId(id);
        cboardRepository.deleteById(id);
        }

    public boolean hasPermission(Long id, String userNick) {
        // 게시물을 ID로 찾습니다.
        Optional<Cboard> optionalCboard = cboardRepository.findById(id);
        System.out.println(optionalCboard);
        // 게시물이 존재하지 않을 경우 권한이 없음을 반환합니다.
        if (optionalCboard.isEmpty()) {
            return false;
        }

        // 게시물을 작성한 사용자의 닉네임과 주어진 사용자 닉네임을 비교하여 권한을 확인합니다.
        Cboard board = optionalCboard.get();
        return board.getNickname().equals(userNick);
    }

}
