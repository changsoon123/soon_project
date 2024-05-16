package com.soon.chat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Message extends BaseModel {

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    private String content;
    private String room;

    private String username;


}
