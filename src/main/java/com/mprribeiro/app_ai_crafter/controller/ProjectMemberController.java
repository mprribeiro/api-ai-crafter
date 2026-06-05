package com.mprribeiro.app_ai_crafter.controller;

import com.mprribeiro.app_ai_crafter.dto.member.InviteMemberRequest;
import com.mprribeiro.app_ai_crafter.dto.member.MemberResponse;
import com.mprribeiro.app_ai_crafter.dto.member.UpdateMemberRequest;
import com.mprribeiro.app_ai_crafter.security.AuthUtil;
import com.mprribeiro.app_ai_crafter.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/members")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;
    private final AuthUtil authUtil;

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getProjectMembers(@PathVariable(name = "projectId") final Long projectId) {
        return ResponseEntity.ok(projectMemberService.getProjectMembers(projectId, getCurrentUserId()));
    }

    @PostMapping
    public ResponseEntity<MemberResponse> inviteMember(@PathVariable(name = "projectId") final Long projectId,
                                                      @RequestBody final InviteMemberRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectMemberService.inviteMember(projectId, getCurrentUserId(), request));
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberResponse> updateMemberRole(@PathVariable(name = "projectId") final Long projectId,
                                                           @PathVariable(name = "memberId") final Long memberId,
                                                           @RequestBody final UpdateMemberRequest request) {
        return ResponseEntity.ok(projectMemberService.updateMemberRole(projectId, getCurrentUserId(), memberId, request));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> removeMember(@PathVariable(name = "projectId") final Long projectId,
                                             @PathVariable(name = "memberId") final Long memberId) {
        projectMemberService.removeMember(projectId, getCurrentUserId(), memberId);
        return ResponseEntity.noContent().build();
    }

    private Long getCurrentUserId() {
        return authUtil.getCurrentUserId();
    }
}
