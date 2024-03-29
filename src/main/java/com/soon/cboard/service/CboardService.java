package com.soon.cboard.service;

import com.soon.cboard.entity.Cboard;
import com.soon.cboard.repository.CboardRepository;
import com.soon.jwt.TokenProvider;
import com.soon.jwt.TokenUserInfo;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public Cboard createBoard(Cboard board, String token) {

        TokenUserInfo userInfo = tokenProvider.validateAndReturnTokenUserInfo(token.substring(7));
        board.setNickname(userInfo.getUserNick());
        board.setTitle(board.getTitle());
        board.setContent(board.getContent());
        board.setCreatedAt(LocalDateTime.now());

        // 파일 URL을 포함하여 게시물을 저장합니다.
        Cboard savedBoard = cboardRepository.save(board);

        // 저장된 게시물을 반환합니다.
        return savedBoard;
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
