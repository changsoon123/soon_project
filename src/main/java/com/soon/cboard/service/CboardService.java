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

    public Cboard createBoard(Cboard board, MultipartFile file, String token) {
        try {
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("파일이 비어 있습니다.");
            }


            String uploadDirectory = "/path/to/upload/directory";
            Path uploadPath = Paths.get(uploadDirectory);
            Files.createDirectories(uploadPath);


            String fileName = Optional.ofNullable(file.getOriginalFilename())
                    .map(StringUtils::cleanPath)
                    .map(name -> UUID.randomUUID().toString() + "_" + name)
                    .orElseThrow(() -> new IllegalArgumentException("파일 이름을 가져올 수 없습니다."));

            // 파일을 서버에 저장
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            }


            board.setFilePath(uploadPath.resolve(fileName).toString());

        } catch (IOException ex) {

            throw new FileUploadException("파일 업로드 중 오류가 발생했습니다.", ex);
        }

        // 게시글 저장
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
