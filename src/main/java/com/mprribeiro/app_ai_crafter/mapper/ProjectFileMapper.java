package com.mprribeiro.app_ai_crafter.mapper;

import com.mprribeiro.app_ai_crafter.dto.project.FileNode;
import com.mprribeiro.app_ai_crafter.entity.ProjectFile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectFileMapper {

    List<FileNode> toListOfFileNode(List<ProjectFile> projectFileList);
}
