package com.soon.cboard.controller;

import com.soon.cboard.entity.Cboard;
import com.soon.cboard.service.CboardService;
import com.soon.jwt.TokenProvider;
import com.soon.jwt.TokenUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public Page<Cboard> getBoardsByPage(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int pageSize) {

        log.info("- page : {}", page);

        PageRequest pageRequest = PageRequest.of(page, pageSize);

        return cboardService.getBoardsByPage(pageRequest);
    }

    @GetMapping("/board/{id}")
    public Cboard getBoardById(@PathVariable Long id) {
        System.out.println("전송 완료");
        return cboardService.getBoardById(id);
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
