package com.mprribeiro.app_ai_crafter.service.impl;

import com.mprribeiro.app_ai_crafter.dto.project.FileContentResponse;
import com.mprribeiro.app_ai_crafter.dto.project.FileNode;
import com.mprribeiro.app_ai_crafter.service.ProjectFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProjectFileServiceImpl implements ProjectFileService {

    @Override
    public List<FileNode> getFileTree(Long projectId, long userId) {
        return List.of();
    }

    @Override
    public FileContentResponse getFileContent(Long projectId, long userId, String path) {
        return null;
    }

    @Override
    public void saveFile(final Long projectId, final String filePath, final String fileContent) {
        log.info("Saving file: {}", filePath);

    }
}
