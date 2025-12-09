package com.SocketOne.One.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
    private String receiver;
}