package com.mprribeiro.app_ai_crafter.service;

import com.mprribeiro.app_ai_crafter.dto.project.FileContentResponse;
import com.mprribeiro.app_ai_crafter.dto.project.FileTreeResponse;

public interface ProjectFileService {

    FileTreeResponse getFileTree(Long projectId, long userId);

    FileContentResponse getFileContent(Long projectId, long userId, String path);

    void saveFile(final Long projectId, final String filePath, final String fileContent);
}
