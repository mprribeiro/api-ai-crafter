package com.mprribeiro.app_ai_crafter.entity;

import com.mprribeiro.app_ai_crafter.enums.PreviewStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatSession {

    ChatSessionId id;

    Instant createdAt;
    Instant updatedAt;

    Instant deletedAt;
}
