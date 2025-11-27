package com.SocketOne.One.Model;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ChatMessage {
    private String content;

    private String sender;

    private MessageType messaageType;
}
