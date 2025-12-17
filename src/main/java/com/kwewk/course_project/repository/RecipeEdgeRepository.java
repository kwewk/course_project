package com.kwewk.course_project.repository;

import com.kwewk.course_project.model.RecipeEdge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeEdgeRepository extends JpaRepository<RecipeEdge, Long> {

    List<RecipeEdge> findByFromStepId(Long fromStepId);

    List<RecipeEdge> findByToStepId(Long toStepId);

    void deleteByFromStepIdOrToStepId(Long fromStepId, Long toStepId);
}