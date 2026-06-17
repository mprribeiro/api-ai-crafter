package com.mprribeiro.app_ai_crafter.service.impl;

import com.mprribeiro.app_ai_crafter.dto.member.InviteMemberRequest;
import com.mprribeiro.app_ai_crafter.dto.member.MemberResponse;
import com.mprribeiro.app_ai_crafter.dto.member.UpdateMemberRequest;
import com.mprribeiro.app_ai_crafter.entity.Project;
import com.mprribeiro.app_ai_crafter.entity.ProjectMember;
import com.mprribeiro.app_ai_crafter.entity.ProjectMemberId;
import com.mprribeiro.app_ai_crafter.exception.ActionNotAllowedException;
import com.mprribeiro.app_ai_crafter.exception.ResourceNotFoundException;
import com.mprribeiro.app_ai_crafter.mapper.ProjectMemberMapper;
import com.mprribeiro.app_ai_crafter.repository.ProjectMemberRepository;
import com.mprribeiro.app_ai_crafter.repository.ProjectRepository;
import com.mprribeiro.app_ai_crafter.repository.UserRepository;
import com.mprribeiro.app_ai_crafter.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberMapper projectMemberMapper;
    private final UserRepository userRepository;

    @Override
    @PreAuthorize("@security.canViewMembers(#projectId)")
    public List<MemberResponse> getProjectMembers(Long projectId, Long userId) {
        return projectMemberRepository.findByIdProjectId(projectId)
                .stream().map(projectMemberMapper::toMemberResponseFromProjectMember).toList();
    }

    @Override
    @PreAuthorize("@security.canManageMembers(#projectId)")
    public MemberResponse inviteMember(final Long projectId,
                                       final Long userId,
                                       final InviteMemberRequest request) {
        final var project = getAcessibleProject(projectId, userId);

        final var user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for username: " + request.username()));

        if (user.getId().equals(userId)) {
            throw new ActionNotAllowedException("User cannot invite themselves to the project");
        }

        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, user.getId());
        if (projectMemberRepository.existsById(projectMemberId)) {
            throw new ActionNotAllowedException("User with username: " + request.username() + " is already a member of the project");
        }

        ProjectMember member = ProjectMember.builder()
                .id(projectMemberId)
                .project(project)
                .user(user)
                .projectRole(request.role())
                .invitedAt(Instant.now())
                .build();

        member = projectMemberRepository.save(member);
        return projectMemberMapper.toMemberResponseFromProjectMember(member);
    }

    @Override
    @PreAuthorize("@security.canManageMembers(#projectId)")
    public MemberResponse updateMemberRole(final Long projectId,
                                           final Long userId,
                                           final Long memberId,
                                           final UpdateMemberRequest request) {
        final var project = getAcessibleProject(projectId, userId);

        ProjectMember member = projectMemberRepository.findById(new ProjectMemberId(projectId, memberId))
                .orElseThrow(() -> new ResourceNotFoundException("Member with id: " + memberId + " is not a member of the project with id: " + projectId));

        member.setProjectRole(request.role());
        member = projectMemberRepository.save(member);
        return projectMemberMapper.toMemberResponseFromProjectMember(member);
    }

    @Override
    @PreAuthorize("@security.canManageMembers(#projectId)")
    public void removeMember(final Long projectId, final Long userId, final Long memberId) {
        final var project = getAcessibleProject(projectId, userId);

        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);
        projectMemberRepository.deleteById(projectMemberId);
    }

    private Project getAcessibleProject(final Long projectId, final Long userId) {
        return projectRepository.findAcessibleByIdAndUserId(projectId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found for id: " + projectId + " and userId: " + userId));
    }
}
