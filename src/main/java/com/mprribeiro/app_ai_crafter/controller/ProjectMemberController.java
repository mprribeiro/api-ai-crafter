package com.mprribeiro.app_ai_crafter.controller;

import com.mprribeiro.app_ai_crafter.dto.member.InviteMemberRequest;
import com.mprribeiro.app_ai_crafter.dto.project.ProjectRequest;
import com.mprribeiro.app_ai_crafter.dto.project.ProjectResponse;
import com.mprribeiro.app_ai_crafter.dto.project.ProjectSummaryResponse;
import com.mprribeiro.app_ai_crafter.entity.ProjectMember;
import com.mprribeiro.app_ai_crafter.service.ProjectMemberService;
import com.mprribeiro.app_ai_crafter.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/members")
public class ProjectMemberController {

    final ProjectMemberService projectMemberService;

    @GetMapping
    public ResponseEntity<List<ProjectMember>> getProjectMembers(@PathVariable(name = "projectId") final Long projectId) {
        final var userId = 1L;
        return ResponseEntity.ok(projectMemberService.getProjectMembers(projectId, userId));
    }

    @PostMapping
    public ResponseEntity<ProjectMember> inviteMember(@PathVariable(name = "projectId") final Long projectId,
                                                      @RequestBody final InviteMemberRequest request) {
        final var userId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectMemberService.inviteMember(projectId, userId, request));
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<ProjectMember> updateMemberRole(@PathVariable(name = "projectId") final Long projectId,
                                                          @PathVariable(name = "memberId") final Long memberId,
                                                          @RequestBody final InviteMemberRequest request) {
        final var userId = 1L;
        return ResponseEntity.ok(projectMemberService.updateMemberRole(projectId, userId, memberId, request));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> removeMember(@PathVariable(name = "projectId") final Long projectId,
                                             @PathVariable(name = "memberId") final Long memberId) {
        final var userId = 1L;
        projectMemberService.removeMember(projectId, userId, memberId);
        return ResponseEntity.noContent().build();
    }
}
