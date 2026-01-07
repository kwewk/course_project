package com.kwewk.course_project.repository;

import com.kwewk.course_project.enums.MealType;
import com.kwewk.course_project.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {

    List<Meal> findByMealType(MealType mealType);

    @Query("SELECT m FROM Meal m WHERE m.name LIKE %:searchTerm%")
    List<Meal> searchByName(@Param("searchTerm") String searchTerm);

    @Query(value = """
    SELECT m.* FROM "Meal" m
    WHERE EXISTS (SELECT 1 FROM "Recipe" r WHERE r."Meal_id" = m."ID")
    ORDER BY (
        SELECT AVG(recipe_calories)
        FROM (
            SELECT SUM(i."Calorie_content" * ri."Quantity" / 100) as recipe_calories
            FROM "Recipe" r
            JOIN "Recipe_Ingredients" ri ON ri."Recipe_id" = r."ID"
            JOIN "Ingredient" i ON i."ID" = ri."Ingredient_id"
            WHERE r."Meal_id" = m."ID"
            GROUP BY r."ID"
        ) recipe_totals
    ) DESC NULLS LAST
    """, nativeQuery = true)
    List<Meal> findAllOrderByCalories();

    @Query("SELECT m FROM Meal m ORDER BY m.rating DESC NULLS LAST")
    List<Meal> findAllOrderByRating();
}