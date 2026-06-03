package com.mprribeiro.app_ai_crafter.service;

import com.mprribeiro.app_ai_crafter.dto.member.InviteMemberRequest;
import com.mprribeiro.app_ai_crafter.dto.member.MemberResponse;
import com.mprribeiro.app_ai_crafter.dto.member.UpdateMemberRequest;

import java.util.List;

public interface ProjectMemberService {
    List<MemberResponse> getProjectMembers(final Long projectId, final Long userId);

    MemberResponse inviteMember(final Long projectId, final Long userId, final InviteMemberRequest request);

    MemberResponse updateMemberRole(final Long projectId,
                                   final Long userId,
                                   final Long memberId,
                                   final UpdateMemberRequest request);

    void removeMember(final Long projectId, final Long userId, final Long memberId);
}
