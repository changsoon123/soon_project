package com.soon.cboard.controller;

import com.soon.cboard.entity.Cboard;
import com.soon.cboard.service.CboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cboard")
public class CboardController {

    @Autowired
    private CboardService cboardService;

    @GetMapping("/boards")
    public List<Cboard> getAllBoards() {
        return cboardService.getAllBoards();
    }

    @GetMapping("/board/{id}")
    public Optional<Cboard> getBoardById(@PathVariable Long id) {
        return cboardService.getBoardById(id);
    }

    @PostMapping("/board")
    public Cboard createBoard(@RequestBody Cboard board) {
        return cboardService.createBoard(board);
    }

    @PutMapping("/board/{id}")
    public Cboard updateBoard(@PathVariable Long id, @RequestBody Cboard updatedBoard) {
        return cboardService.updateBoard(id, updatedBoard);
    }

    @DeleteMapping("/board/{id}")
    public void deleteBoard(@PathVariable Long id) {
        cboardService.deleteBoard(id);
    }
}
