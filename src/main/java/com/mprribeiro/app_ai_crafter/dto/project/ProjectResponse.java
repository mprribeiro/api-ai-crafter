package com.mprribeiro.app_ai_crafter.dto.project;

import java.time.Instant;

public record ProjectResponse(Long id,
                              String name,
                              Instant createdAt,
                              Instant updatedAt) {
}
