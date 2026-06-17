package com.mprribeiro.app_ai_crafter.security;

import com.mprribeiro.app_ai_crafter.enums.ProjectPermission;
import com.mprribeiro.app_ai_crafter.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import static com.mprribeiro.app_ai_crafter.enums.ProjectPermission.*;

@Component("security")
@RequiredArgsConstructor
public class SecurityExpressions {

    private final ProjectMemberRepository projectMemberRepository;
    private final AuthUtil authUtil;

    private boolean hasPermission(final Long projectId,
                                  final ProjectPermission requiredPermission) {
        var userId = authUtil.getCurrentUserId();
        return projectMemberRepository.findRoleByProjectIdAndUserId(projectId, userId)
                .map(role -> role.getPermissions().contains(requiredPermission))
                .orElse(false);
    }

    public boolean canViewProject(final Long projectId) {
        return hasPermission(projectId, VIEW);
    }

    public boolean canEditProject(final Long projectId) {
        return hasPermission(projectId, EDIT);
    }

    public boolean canDeleteProject(final Long projectId) {
        return hasPermission(projectId, DELETE);
    }

    public boolean canViewMembers(final Long projectId) {
        return hasPermission(projectId, VIEW_MEMBERS);
    }

    public boolean canManageMembers(final Long projectId) {
        return hasPermission(projectId, MANAGE_MEMBERS);
    }
}
