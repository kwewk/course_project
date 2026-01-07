package com.kwewk.course_project.repository;

import com.kwewk.course_project.model.MenuMeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuMealRepository extends JpaRepository<MenuMeal, Long> {

    @Query(value = """
        SELECT 
            i."Name" as ingredient_name,
            SUM(ri."Quantity") as required_quantity,
            i."Unit" as unit,
            COALESCE(ui."Quantity", 0) as available_quantity,
            (SUM(ri."Quantity") - COALESCE(ui."Quantity", 0)) as missing_quantity
        FROM "Menu_Meal" mm
        JOIN "Meal" m ON m."ID" = mm."Meal_id"
        JOIN "Recipe" r ON r."Meal_id" = m."ID"
        JOIN "Recipe_Ingredients" ri ON ri."Recipe_id" = r."ID"
        JOIN "Ingredient" i ON i."ID" = ri."Ingredient_id"
        LEFT JOIN "Ingredient" ui ON ui."Name" = i."Name" AND ui."User_id" = :userId
        WHERE mm."Menu_id" = :menuId
        GROUP BY i."Name", i."Unit", ui."Quantity"
        HAVING SUM(ri."Quantity") > COALESCE(ui."Quantity", 0)
        """, nativeQuery = true)
    List<Object[]> generateShoppingList(@Param("menuId") Long menuId, @Param("userId") Long userId);
}