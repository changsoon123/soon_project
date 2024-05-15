package com.soon.handler;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SocketIOEventHandler {

    private final SocketIOServer server;

    @Autowired
    public SocketIOEventHandler(SocketIOServer server) {
        this.server = server;
    }

    @OnConnect
    public void onConnect() {
        System.out.println("클라이언트와 연결");
    }

    @OnDisconnect
    public void onDisconnect() {
        System.out.println("클라이언트와 연결 해제");
    }

    @OnEvent("chatMessage")
    public void onChatMessage(SocketIOClient client, String message) {
        System.out.println("클라이언트 메세지: " + message);

    }
}
