package com.mprribeiro.app_ai_crafter.service;

import com.mprribeiro.app_ai_crafter.dto.member.InviteMemberRequest;
import com.mprribeiro.app_ai_crafter.entity.ProjectMember;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectMemberService {
    List<ProjectMember> getProjectMembers(final Long projectId, final Long userId);

    ProjectMember inviteMember(final Long projectId, final Long userId, final InviteMemberRequest request);

    ProjectMember updateMemberRole(final Long projectId,
                                   final Long userId,
                                   final Long memberId,
                                   final InviteMemberRequest request);

    void removeMember(final Long projectId, final Long userId, final Long memberId);
}
