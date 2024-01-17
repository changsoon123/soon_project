package com.soon.cboard.service;


import com.soon.cboard.entity.Cboard;
import com.soon.cboard.repository.CboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Cboard createBoard(Cboard board) {
        board.setCreatedAt(new Date()); // Set the creation date
        return cboardRepository.save(board);
    }

    public Cboard updateBoard(Long id, Cboard updatedBoard) {
        Optional<Cboard> existingBoardOptional = cboardRepository.findById(id);

        if (existingBoardOptional.isPresent()) {
            Cboard existingBoard = existingBoardOptional.get();
            existingBoard.setTitle(updatedBoard.getTitle());
            existingBoard.setContent(updatedBoard.getContent());
            return cboardRepository.save(existingBoard);
        } else {
            // Handle not found
            return null;
        }
    }

    public void deleteBoard(Long id) {
        cboardRepository.deleteById(id);
    }

}
