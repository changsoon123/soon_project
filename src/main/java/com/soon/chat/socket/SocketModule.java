package com.soon.chat.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.soon.chat.constants.Constants;
import com.soon.chat.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SocketModule {


    private final SocketIOServer server;
    private final SocketService socketService;

    public SocketModule(SocketIOServer server, SocketService socketService) {
        this.server = server;
        this.socketService = socketService;
        server.addConnectListener(onConnected());
        server.addDisconnectListener(onDisconnected());
        server.addEventListener("send_message", Message.class, onChatReceived());

    }


    private DataListener<Message> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            socketService.saveMessage(senderClient, data);
        };
    }


    private ConnectListener onConnected() {
        return (client) -> {

            Map<String, List<String>> params = client.getHandshakeData().getUrlParams();

            // 방 이름 가져오기
            String room = params.getOrDefault("room", Collections.emptyList())
                    .stream().findFirst().orElse("defaultRoom");

            // 사용자 이름 가져오기
            String username = params.getOrDefault("username", Collections.singletonList("Guest"))
                    .stream().findFirst().orElse("Guest");
            client.joinRoom(room);

            log.info("Socket ID[{}] - room[{}] - username [{}]  Connected to chat module through", client.getSessionId().toString(), room, username);
        };

    }

    private DisconnectListener onDisconnected() {
        return client -> {
            Map<String, List<String>> params = client.getHandshakeData().getUrlParams();

            // 방 이름 가져오기
            String room = params.getOrDefault("room", Collections.emptyList())
                    .stream().findFirst().orElse("defaultRoom");

            // 사용자 이름 가져오기
            String username = params.getOrDefault("username", Collections.singletonList("Guest"))
                    .stream().findFirst().orElse("Guest");

            log.info("Socket ID[{}] - room[{}] - username [{}]  discnnected to chat module through", client.getSessionId().toString(), room, username);
        };
    }


}
