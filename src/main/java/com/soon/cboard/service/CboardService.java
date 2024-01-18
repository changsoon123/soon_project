package com.soon.cboard.service;


import com.soon.cboard.dto.CboardDto;
import com.soon.cboard.entity.Cboard;
import com.soon.cboard.repository.CboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CboardService {

    @Autowired
    private CboardRepository cboardRepository;

    public List<Cboard> getAllBoards() {
        return cboardRepository.findAll();
    }

    public Optional<Cboard> getBoardById(Long id) {
        return cboardRepository.findById(id);
    }

    public Cboard createBoard(CboardDto boardDto) {
        Cboard board = new Cboard();
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setCreatedAt(new Date());
        return cboardRepository.save(board);
    }

    public Cboard updateBoard(Long id, CboardDto updatedBoardDto) {
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
