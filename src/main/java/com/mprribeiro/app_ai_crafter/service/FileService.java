package com.mprribeiro.app_ai_crafter.service;

import com.mprribeiro.app_ai_crafter.dto.project.FileContentResponse;
import com.mprribeiro.app_ai_crafter.dto.project.FileNode;

import java.util.List;

public interface FileService {

    List<FileNode> getFileTree(Long projectId, long userId);

    FileContentResponse getFileContent(Long projectId, long userId, String path);
}
