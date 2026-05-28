package com.mprribeiro.app_ai_crafter.dto.project;

import java.time.Instant;

public record FileNode(String path, Instant modifiedAt, Long size, String type) {
}
