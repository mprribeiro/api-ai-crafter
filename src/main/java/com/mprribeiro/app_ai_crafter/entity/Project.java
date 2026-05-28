package com.mprribeiro.app_ai_crafter.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Project {

    Long id;

    String name;

    Boolean isPublic = false;

    Instant createdAt;
    Instant updatedAt;

    Instant deletedAt;
}
