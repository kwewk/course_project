package com.kwewk.course_project.repository;

import com.kwewk.course_project.model.RecipeEdge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeEdgeRepository extends JpaRepository<RecipeEdge, Long> {

    void deleteByFromStepIdOrToStepId(Long fromStepId, Long toStepId);
}