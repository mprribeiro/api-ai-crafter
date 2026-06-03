package com.mprribeiro.app_ai_crafter.mapper;

import com.mprribeiro.app_ai_crafter.dto.project.ProjectResponse;
import com.mprribeiro.app_ai_crafter.dto.project.ProjectSummaryResponse;
import com.mprribeiro.app_ai_crafter.entity.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectResponse toProjectResponse(Project project);

    ProjectSummaryResponse toProjectSummaryResponse(Project project);
}
