package com.mprribeiro.app_ai_crafter.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectMember {

    ProjectMemberId id;

    Project project;

    ProjectRole projectRole;

    Instant invitedAt;
    Instant acceptedAt;
}
