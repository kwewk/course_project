package com.kwewk.course_project.repository;

import com.kwewk.course_project.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findByUserId(Long userId);

    @Query("SELECT i FROM Ingredient i WHERE i.user IS NULL")
    List<Ingredient> findCommonIngredients();

    @Query("SELECT i FROM Ingredient i WHERE i.user.id = :userId AND i.quantity < :threshold ORDER BY i.quantity ASC")
    List<Ingredient> findLowStockIngredients(@Param("userId") Long userId, @Param("threshold") Integer threshold);
}