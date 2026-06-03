package com.mprribeiro.app_ai_crafter.repository;

import com.mprribeiro.app_ai_crafter.entity.Project;
import com.mprribeiro.app_ai_crafter.entity.ProjectMember;
import com.mprribeiro.app_ai_crafter.entity.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {

    List<ProjectMember> findByIdProjectId(Long projectId);
}
