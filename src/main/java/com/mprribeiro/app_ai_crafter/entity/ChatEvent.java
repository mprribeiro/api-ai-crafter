package com.mprribeiro.app_ai_crafter.entity;

import com.mprribeiro.app_ai_crafter.enums.ChatEventStatus;
import com.mprribeiro.app_ai_crafter.enums.ChatEventType;
import com.mprribeiro.app_ai_crafter.enums.MessageRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatEvent {

    Long id;

    ChatMessage chatMessage;

    ChatEventType type;

    Integer sequenceOrder;

    String content;
    String filePath;
    String metadata;
    String sagaId;

    ChatEventStatus status;

}