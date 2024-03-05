package com.soon.cboard.service;

import com.soon.cboard.entity.Cboard;
import com.soon.cboard.repository.CboardRepository;
import com.soon.jwt.TokenProvider;
import com.soon.jwt.TokenUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CboardService {

    @Autowired
    private CboardRepository cboardRepository;

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

    public Cboard createBoard(Cboard boardDto, String token) {
        TokenUserInfo userInfo = tokenProvider.validateAndReturnTokenUserInfo(token.substring(7));
        Cboard board = new Cboard();
        board.setNickname(userInfo.getUserNick());
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setCreatedAt(new Date());
        return cboardRepository.save(board);
    }

    public Cboard updateBoard(Long id, Cboard updatedBoardDto) {
        Optional<Cboard> existingBoardOptional = cboardRepository.findById(id);

        if (existingBoardOptional.isPresent()) {
            Cboard existingBoard = existingBoardOptional.get();
            existingBoard.setTitle(updatedBoardDto.getTitle());
            existingBoard.setContent(updatedBoardDto.getContent());
            return cboardRepository.save(existingBoard);
        } else {
            // 찾지 못했을 때 처리
            return null;
        }
    }

    public void deleteBoard(Long id) {
        cboardRepository.deleteById(id);
    }

}
