package com.mprribeiro.app_ai_crafter.repository;

import com.mprribeiro.app_ai_crafter.entity.ProjectMember;
import com.mprribeiro.app_ai_crafter.entity.ProjectMemberId;
import com.mprribeiro.app_ai_crafter.enums.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {

    List<ProjectMember> findByIdProjectId(Long projectId);

    @Query("""
        SELECT pm.projectRole
        FROM ProjectMember pm
        WHERE pm.id.projectId = :projectId
        AND pm.id.userId = :userId
    """)
    Optional<ProjectRole> findRoleByProjectIdAndUserId(@Param("projectId") Long projectId,
                                                       @Param("userId") Long userId);

    @Query("""
        SELECT count(1)
        FROM ProjectMember pm
        WHERE pm.id.userId = :userId
        AND pm.role = 'OWNER'
    """)
    Integer countProjectOwnedByUser(@Param("userId") Long userId);
}
