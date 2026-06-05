package com.mprribeiro.app_ai_crafter.repository;

import com.mprribeiro.app_ai_crafter.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("""
        SELECT p FROM Project p
        WHERE p.deletedAt IS NULL
        AND EXISTS (
            SELECT 1 FROM ProjectMember pm
            WHERE pm.id.projectId = p.id
            AND pm.id.userId = :userId
        )
        ORDER BY p.updatedAt DESC
    """)
    List<Project> findAllAcessibleByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT p FROM Project p
        WHERE p.deletedAt IS NULL
        AND p.id = :projectId
        AND EXISTS (
            SELECT 1 FROM ProjectMember pm
            WHERE pm.id.projectId = :projectId
            AND pm.id.userId = :userId
        )
    """)
    Optional<Project> findAcessibleByIdAndUserId(@Param("projectId") Long projectId,
                                                 @Param("userId") Long userId);
}
