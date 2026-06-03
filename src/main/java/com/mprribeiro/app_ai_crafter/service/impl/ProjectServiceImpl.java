package com.mprribeiro.app_ai_crafter.service.impl;

import com.mprribeiro.app_ai_crafter.dto.project.ProjectRequest;
import com.mprribeiro.app_ai_crafter.dto.project.ProjectResponse;
import com.mprribeiro.app_ai_crafter.dto.project.ProjectSummaryResponse;
import com.mprribeiro.app_ai_crafter.entity.Project;
import com.mprribeiro.app_ai_crafter.entity.ProjectMember;
import com.mprribeiro.app_ai_crafter.entity.ProjectMemberId;
import com.mprribeiro.app_ai_crafter.enums.ProjectRole;
import com.mprribeiro.app_ai_crafter.exception.ProjectNotFoundException;
import com.mprribeiro.app_ai_crafter.exception.UserNotFoundException;
import com.mprribeiro.app_ai_crafter.mapper.ProjectMapper;
import com.mprribeiro.app_ai_crafter.repository.ProjectMemberRepository;
import com.mprribeiro.app_ai_crafter.repository.ProjectRepository;
import com.mprribeiro.app_ai_crafter.repository.UserRepository;
import com.mprribeiro.app_ai_crafter.service.ProjectService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

    @Override
    public ProjectResponse getUserProjectById(final Long id, final Long userId) {
        final var project =  projectRepository.findAcessibleByIdAndUserId(id, userId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found for id: " + id));

        return mapper.toProjectResponse(project);
    }

    @Override
    public List<ProjectSummaryResponse> getUserProjects(final Long userId) {
        List<Project> projects = projectRepository.findAllAcessibleByUserId(userId);
        return projects.stream().map(mapper::toProjectSummaryResponse).toList();
    }

    @Override
    public void deleteUserProjectById(final Long id, final Long userId) {
        Project project = getAcessibleProject(id, userId);

        project.setDeletedAt(Instant.now());
        projectRepository.save(project);
    }

    @Override
    public ProjectResponse createProject(final ProjectRequest request, final Long userId) {
        final var owner = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));

        Project project = Project.builder()
                .name(request.name())
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
    public ProjectResponse updateProject(final Long id, final ProjectRequest request, final Long userId) {
        Project project = getAcessibleProject(id, userId);

        project.setName(request.name());
        project = projectRepository.save(project);

        return mapper.toProjectResponse(project);
    }

    private Project getAcessibleProject(final Long id, final Long userId) {
        return projectRepository.findAcessibleById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found for id: " + id));
    }
}
