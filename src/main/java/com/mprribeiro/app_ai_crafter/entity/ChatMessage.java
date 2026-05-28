package com.mprribeiro.app_ai_crafter.entity;

import com.mprribeiro.app_ai_crafter.enums.MessageRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessage {

    Long id;
    ChatSession chatSession;

    MessageRole role;

    List<ChatEvent> events;

    String content;

    Integer tokensUsed = 0;

    Instant createdAt;
}
