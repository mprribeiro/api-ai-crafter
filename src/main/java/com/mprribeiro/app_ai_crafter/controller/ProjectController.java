package com.mprribeiro.app_ai_crafter.controller;

import com.mprribeiro.app_ai_crafter.dto.project.ProjectRequest;
import com.mprribeiro.app_ai_crafter.dto.project.ProjectResponse;
import com.mprribeiro.app_ai_crafter.dto.project.ProjectSummaryResponse;
import com.mprribeiro.app_ai_crafter.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectSummaryResponse>> getUserProjects() {
        final var userId = 1L;
        return ResponseEntity.ok(projectService.getUserProjects(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable(name = "id") final Long id) {
        final var userId = 1L;
        return ResponseEntity.ok(projectService.getUserProjectById(id, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable(name = "id") final Long id) {
        final var userId = 1L;
        projectService.deleteUserProjectById(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable(name = "id") final Long id,
                                                         @RequestBody @Valid final ProjectRequest request) {
        final var userId = 1L;
        return ResponseEntity.ok(projectService.updateProject(id, request, userId));
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody @Valid final ProjectRequest request) {
        final var userId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(request, userId));
    }

}
