package com.kwewk.course_project.repository;

import com.kwewk.course_project.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByUserId(Long userId);

    Optional<Menu> findByUserIdAndDate(Long userId, LocalDate date);

    List<Menu> findByUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate);

    @Query(value = """
        SELECT m."Name" as meal_name, COUNT(*) as frequency
        FROM "Menu" menu
        JOIN "Menu_Meal" mm ON mm."Menu_id" = menu."ID"
        JOIN "Meal" m ON m."ID" = mm."Meal_id"
        WHERE menu."User_id" = :userId
        AND menu."Date" BETWEEN :startDate AND :endDate
        GROUP BY m."Name"
        ORDER BY frequency DESC
        LIMIT :limit
        """, nativeQuery = true)
    List<Object[]> findMostFrequentMeals(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("limit") int limit
    );
}