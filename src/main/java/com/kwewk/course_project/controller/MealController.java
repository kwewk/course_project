package com.kwewk.course_project.controller;

import com.kwewk.course_project.dto.MealDTO;
import com.kwewk.course_project.dto.response.ApiResponse;
import com.kwewk.course_project.enums.MealType;
import com.kwewk.course_project.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MealDTO>>> searchMeals(@RequestParam String query) {
        List<MealDTO> meals = mealService.searchMealsByName(query);
        return ResponseEntity.ok(ApiResponse.success(meals));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MealDTO>>> getAllMeals() {
        List<MealDTO> meals = mealService.getAllMeals();
        return ResponseEntity.ok(ApiResponse.success(meals));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MealDTO>> getMealById(@PathVariable Long id) {
        MealDTO meal = mealService.getMealById(id);
        return ResponseEntity.ok(ApiResponse.success(meal));
    }

    @GetMapping("/ranked/by-calories")
    public ResponseEntity<ApiResponse<List<MealDTO>>> getMealsOrderedByCalories() {
        List<MealDTO> meals = mealService.getMealsOrderedByCalories();
        return ResponseEntity.ok(ApiResponse.success(meals));
    }

    @GetMapping("/ranked/by-rating")
    public ResponseEntity<ApiResponse<List<MealDTO>>> getMealsOrderedByRating() {
        List<MealDTO> meals = mealService.getMealsOrderedByRating();
        return ResponseEntity.ok(ApiResponse.success(meals));
    }

    @GetMapping("/by-type")
    public ResponseEntity<ApiResponse<List<MealDTO>>> getMealsByType(@RequestParam MealType type) {
        List<MealDTO> meals = mealService.getMealsByType(type);
        return ResponseEntity.ok(ApiResponse.success(meals));
    }
}