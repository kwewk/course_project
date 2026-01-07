package com.kwewk.course_project.repository;

import com.kwewk.course_project.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findByUserId(Long userId);

    List<Recipe> findByMealId(Long mealId);

    @Query("SELECT r FROM Recipe r WHERE r.meal.name LIKE %:mealName%")
    List<Recipe> searchByMealName(@Param("mealName") String mealName);

    @Query("""
        SELECT DISTINCT r FROM Recipe r
        JOIN r.ingredients ri
        JOIN ri.ingredient i
        WHERE i.name LIKE %:ingredientName%
        """)
    List<Recipe> searchByIngredientName(@Param("ingredientName") String ingredientName);
}