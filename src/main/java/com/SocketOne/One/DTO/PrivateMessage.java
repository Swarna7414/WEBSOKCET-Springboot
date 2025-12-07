package com.SocketOne.One.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PrivateMessage {

    @NotBlank(message = "Sender Must not be empty")
    @Size(max = 20, message = "sender must not be more than 20")
    private String sender;

    @NotBlank(message = "Recipent cannnot be empty")
    @Size(max = 20, message = "Recipient must be at most 20 characters")
    private String recipient;

    @NotBlank(message = "Message cannot be empty")
    @Size(max = 200, message = "Message must be at most 2000")
    private String content;

    private Instant timestamp;
}