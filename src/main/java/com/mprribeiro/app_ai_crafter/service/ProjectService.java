package com.mprribeiro.app_ai_crafter.service;

import com.mprribeiro.app_ai_crafter.dto.project.ProjectRequest;
import com.mprribeiro.app_ai_crafter.dto.project.ProjectResponse;
import com.mprribeiro.app_ai_crafter.dto.project.ProjectSummaryResponse;

import java.util.List;

public interface ProjectService {

    ProjectResponse getUserProjectById(final Long id);

    List<ProjectSummaryResponse> getUserProjects();

    void deleteProjectById(final Long id);

    ProjectResponse createProject(final ProjectRequest request);

    ProjectResponse updateProject(final Long id, final ProjectRequest request);
}
