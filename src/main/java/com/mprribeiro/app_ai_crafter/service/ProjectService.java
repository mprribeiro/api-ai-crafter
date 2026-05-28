package com.mprribeiro.app_ai_crafter.service;

import com.mprribeiro.app_ai_crafter.dto.project.ProjectRequest;
import com.mprribeiro.app_ai_crafter.dto.project.ProjectResponse;
import com.mprribeiro.app_ai_crafter.dto.project.ProjectSummaryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService {

    ProjectResponse getUserProjectById(final Long id, final Long userId);

    List<ProjectSummaryResponse> getUserProjects(final Long userId);

    void deleteUserProjectById(final Long id, final Long userId);

    ProjectResponse createProject(final ProjectRequest request, final Long userId);

    ProjectResponse updateProject(final ProjectRequest request, final Long userId);
}
