package com.soon.cboard.controller;

import com.soon.cboard.entity.Cboard;
import com.soon.cboard.service.CboardService;
import com.soon.jwt.TokenProvider;
import com.soon.jwt.TokenUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cboard")
public class CboardController {

    @Autowired
    private CboardService cboardService;


    @GetMapping("/boards")
    public List<Cboard> getAllBoards() {
        return cboardService.getAllBoards();
    }

    @GetMapping("/boards/{page}")
    public Slice<Cboard> getBoardsByPage(@PathVariable int page) {
        int pageSize = 10; // 페이지당 게시물 수
        return cboardService.getBoardsByPage(page, pageSize);
    }

    @PostMapping("/board")
    public Cboard createBoard(@RequestBody Cboard board, @RequestHeader("Authorization") String token) {
        return cboardService.createBoard(board,token);
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
