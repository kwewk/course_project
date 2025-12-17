package com.kwewk.course_project.repository;

import com.kwewk.course_project.model.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

    List<RecipeIngredient> findByRecipeId(Long recipeId);

    List<RecipeIngredient> findByIngredientId(Long ingredientId);

    @Query(value = """
        SELECT 
            i."Name" as ingredient_name,
            ri."Quantity" as required_quantity,
            COALESCE(ui."Quantity", 0) as available_quantity,
            (ri."Quantity" - COALESCE(ui."Quantity", 0)) as missing_quantity
        FROM "Recipe_Ingredients" ri
        JOIN "Ingredient" i ON i."ID" = ri."Ingredient_id"
        LEFT JOIN "Ingredient" ui ON ui."Name" = i."Name" AND ui."User_id" = :userId
        WHERE ri."Recipe_id" = :recipeId
        AND (ui."Quantity" IS NULL OR ui."Quantity" < ri."Quantity")
        """, nativeQuery = true)
    List<Object[]> findMissingIngredients(@Param("recipeId") Long recipeId, @Param("userId") Long userId);
}