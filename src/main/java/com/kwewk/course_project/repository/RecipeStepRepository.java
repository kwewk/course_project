package com.kwewk.course_project.repository;

import com.kwewk.course_project.model.RecipeStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {

    List<RecipeStep> findByRecipeId(Long recipeId);

    @Query("""
        SELECT rs FROM RecipeStep rs
        WHERE rs.recipe.id = :recipeId
        AND rs.incomingEdges IS EMPTY
        """)
    List<RecipeStep> findStartingSteps(@Param("recipeId") Long recipeId);
}