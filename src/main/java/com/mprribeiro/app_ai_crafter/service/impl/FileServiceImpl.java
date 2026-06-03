package com.mprribeiro.app_ai_crafter.service.impl;

import com.mprribeiro.app_ai_crafter.dto.project.FileContentResponse;
import com.mprribeiro.app_ai_crafter.dto.project.FileNode;
import com.mprribeiro.app_ai_crafter.service.FileService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public List<FileNode> getFileTree(Long projectId, long userId) {
        return List.of();
    }

    @Override
    public FileContentResponse getFileContent(Long projectId, long userId, String path) {
        return null;
    }
}
