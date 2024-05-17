package com.soon.chat.controller;

import com.soon.chat.entity.Message;
import com.soon.chat.service.MessageService;
import com.soon.jwt.TokenProvider;
import com.soon.jwt.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @Autowired
    private TokenProvider tokenProvider;

    @CrossOrigin
    @GetMapping("/{room}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable String room) {
        return ResponseEntity.ok(messageService.getMessages(room));
    }

    @GetMapping("/message/userinfo")
    public ResponseEntity<?> checkPermission( @RequestHeader("Authorization") String token) {


        TokenUserInfo userInfo = tokenProvider.validateAndReturnTokenUserInfo(token.substring(7));


        return ResponseEntity.ok(userInfo.getUserNick());
    }

}
