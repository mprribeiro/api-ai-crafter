package com.mprribeiro.app_ai_crafter.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsageLog {
    Long id;
    User user;
    Project project;
    String action;
    Integer tokensUsed;
    Integer durationMs;
    String metadata;
    Instant createdAt;
}
