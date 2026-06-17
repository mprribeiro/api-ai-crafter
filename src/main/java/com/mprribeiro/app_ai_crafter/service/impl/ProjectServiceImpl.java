package com.mprribeiro.app_ai_crafter.service.impl;

import com.mprribeiro.app_ai_crafter.dto.project.ProjectRequest;
import com.mprribeiro.app_ai_crafter.dto.project.ProjectResponse;
import com.mprribeiro.app_ai_crafter.dto.project.ProjectSummaryResponse;
import com.mprribeiro.app_ai_crafter.entity.Project;
import com.mprribeiro.app_ai_crafter.entity.ProjectMember;
import com.mprribeiro.app_ai_crafter.entity.ProjectMemberId;
import com.mprribeiro.app_ai_crafter.enums.ProjectRole;
import com.mprribeiro.app_ai_crafter.exception.BadRequestException;
import com.mprribeiro.app_ai_crafter.exception.ResourceNotFoundException;
import com.mprribeiro.app_ai_crafter.mapper.ProjectMapper;
import com.mprribeiro.app_ai_crafter.repository.ProjectMemberRepository;
import com.mprribeiro.app_ai_crafter.repository.ProjectRepository;
import com.mprribeiro.app_ai_crafter.repository.UserRepository;
import com.mprribeiro.app_ai_crafter.security.AuthUtil;
import com.mprribeiro.app_ai_crafter.service.ProjectService;
import com.mprribeiro.app_ai_crafter.service.SubscriptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper mapper;
    private final ProjectMemberRepository projectMemberRepository;
    private final AuthUtil authUtil;
    private final SubscriptionService subscriptionService;

    @Override
    @PreAuthorize("@security.canViewProject(#id)")
    public ProjectResponse getUserProjectById(final Long id) {
        final var userId = authUtil.getCurrentUserId();
        final var project =  projectRepository.findAcessibleByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found for id: " + id));

        return mapper.toProjectResponse(project);
    }

    @Override
    public List<ProjectSummaryResponse> getUserProjects() {
        final var userId = authUtil.getCurrentUserId();
        List<Project> projects = projectRepository.findAllAcessibleByUserId(userId);
        return projects.stream().map(mapper::toProjectSummaryResponse).toList();
    }

    @Override
    @PreAuthorize("@security.canDeleteProject(#id)")
    public void deleteProjectById(final Long id) {
        final var userId = authUtil.getCurrentUserId();
        Project project = getAcessibleProject(id, userId);

        project.setDeletedAt(Instant.now());
        projectRepository.save(project);
    }

    @Override
    public ProjectResponse createProject(final ProjectRequest request) {

        if (!subscriptionService.canCreateNewProject()) {
            throw new BadRequestException("User cannot create new project with current plan!");
        }

        final var userId = authUtil.getCurrentUserId();
        final var owner = userRepository.getReferenceById(userId);

        Project project = Project.builder()
                .name(request.name())
                .isPublic(false)
                .build();
        project = projectRepository.save(project);


        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), owner.getId());
        ProjectMember projectMember = ProjectMember.builder()
                .id(projectMemberId)
                .project(project)
                .user(owner)
                .projectRole(ProjectRole.OWNER)
                .invitedAt(Instant.now())
                .acceptedAt(Instant.now())
                .build();
        projectMemberRepository.save(projectMember);

        return mapper.toProjectResponse(project);
    }

    @Override
    @PreAuthorize("@security.canEditProject(#id)")
    public ProjectResponse updateProject(final Long id, final ProjectRequest request) {
        final var userId = authUtil.getCurrentUserId();
        Project project = getAcessibleProject(id, userId);

        project.setName(request.name());
        project = projectRepository.save(project);

        return mapper.toProjectResponse(project);
    }

    private Project getAcessibleProject(final Long id, final Long userId) {
        return projectRepository.findAcessibleByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found for id: " + id));
    }
}
