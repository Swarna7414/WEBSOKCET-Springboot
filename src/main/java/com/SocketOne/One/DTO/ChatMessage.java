package com.SocketOne.One.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ChatMessage {

    @NotBlank(message = "Sender name cannot be empty")
    @Size(max = 20, message = "Sender name must be at most 20 characters")
    private String sender;

    @NotBlank(message = "Message cannot be empty")
    @Size(max = 200, message = "Message must be at most 200 characters")
    private String content;

    private Instant timestamp;

    private MessageType type;


}