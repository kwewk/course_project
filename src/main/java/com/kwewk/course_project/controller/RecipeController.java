package com.kwewk.course_project.controller;

import com.kwewk.course_project.dto.RecipeDTO;
import com.kwewk.course_project.dto.request.CreateRecipeRequest;
import com.kwewk.course_project.dto.response.ApiResponse;
import com.kwewk.course_project.service.RecipeCheckService;
import com.kwewk.course_project.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final RecipeCheckService recipeCheckService;

    @PostMapping
    public ResponseEntity<ApiResponse<RecipeDTO>> createRecipe(
            @Valid @RequestBody CreateRecipeRequest request,
            @RequestHeader("User-Id") Long userId) {
        RecipeDTO recipe = recipeService.createRecipe(request, userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Recipe created successfully", recipe));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RecipeDTO>> updateRecipe(
            @PathVariable Long id,
            @Valid @RequestBody CreateRecipeRequest request,
            @RequestHeader("User-Id") Long userId) {
        RecipeDTO recipe = recipeService.updateRecipe(id, request, userId);
        return ResponseEntity.ok(ApiResponse.success("Recipe updated successfully", recipe));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRecipe(
            @PathVariable Long id,
            @RequestHeader("User-Id") Long userId) {
        recipeService.deleteRecipe(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Recipe deleted successfully", null));
    }

    @GetMapping("/search/by-ingredient")
    public ResponseEntity<ApiResponse<List<RecipeDTO>>> searchByIngredient(@RequestParam String ingredient) {
        List<RecipeDTO> recipes = recipeService.searchRecipesByIngredient(ingredient);
        return ResponseEntity.ok(ApiResponse.success(recipes));
    }

    @GetMapping("/search/by-meal")
    public ResponseEntity<ApiResponse<List<RecipeDTO>>> searchByMealName(@RequestParam String mealName) {
        List<RecipeDTO> recipes = recipeService.searchRecipesByMealName(mealName);
        return ResponseEntity.ok(ApiResponse.success(recipes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RecipeDTO>> getRecipeById(@PathVariable Long id) {
        RecipeDTO recipe = recipeService.getRecipeById(id);
        return ResponseEntity.ok(ApiResponse.success(recipe));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<RecipeDTO>>> getUserRecipes(@PathVariable Long userId) {
        List<RecipeDTO> recipes = recipeService.getUserRecipes(userId);
        return ResponseEntity.ok(ApiResponse.success(recipes));
    }

    @GetMapping("/meal/{mealId}")
    public ResponseEntity<ApiResponse<List<RecipeDTO>>> getMealRecipes(@PathVariable Long mealId) {
        List<RecipeDTO> recipes = recipeService.getMealRecipes(mealId);
        return ResponseEntity.ok(ApiResponse.success(recipes));
    }

    @PostMapping("/{id}/favorite")
    public ResponseEntity<ApiResponse<Void>> addToFavorites(
            @PathVariable Long id,
            @RequestHeader("User-Id") Long userId) {
        recipeService.addToFavorites(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Recipe added to favorites", null));
    }

    @DeleteMapping("/{id}/favorite")
    public ResponseEntity<ApiResponse<Void>> removeFromFavorites(
            @PathVariable Long id,
            @RequestHeader("User-Id") Long userId) {
        recipeService.removeFromFavorites(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Recipe removed from favorites", null));
    }

    @GetMapping("/favorites")
    public ResponseEntity<ApiResponse<List<RecipeDTO>>> getFavoriteRecipes(
            @RequestHeader("User-Id") Long userId) {
        List<RecipeDTO> recipes = recipeService.getFavoriteRecipes(userId);
        return ResponseEntity.ok(ApiResponse.success(recipes));
    }

    @GetMapping("/{id}/check-availability")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkRecipeAvailability(
            @PathVariable Long id,
            @RequestHeader("User-Id") Long userId) {
        Map<String, Object> result = recipeCheckService.checkRecipeAvailability(id, userId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}