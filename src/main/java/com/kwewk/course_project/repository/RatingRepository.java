package com.kwewk.course_project.repository;

import com.kwewk.course_project.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByMealId(Long mealId);

    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.meal.id = :mealId")
    Double calculateAverageRating(@Param("mealId") Long mealId);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Rating r WHERE r.meal.id = :mealId AND r.user.id = :userId")
    boolean hasUserRatedMeal(@Param("mealId") Long mealId, @Param("userId") Long userId);
}