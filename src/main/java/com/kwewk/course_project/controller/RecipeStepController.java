package com.kwewk.course_project.controller;

import com.kwewk.course_project.dto.RecipeStepDTO;
import com.kwewk.course_project.dto.response.ApiResponse;
import com.kwewk.course_project.service.RecipeStepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipe-steps")
@RequiredArgsConstructor
public class RecipeStepController {

    private final RecipeStepService recipeStepService;

    @PostMapping
    public ResponseEntity<ApiResponse<RecipeStepDTO>> addStep(
            @RequestParam Long recipeId,
            @RequestParam String description,
            @RequestHeader("User-Id") Long userId) {
        RecipeStepDTO step = recipeStepService.addStep(recipeId, description, userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Step added successfully", step));
    }

    @PostMapping("/dependency")
    public ResponseEntity<ApiResponse<Void>> addStepDependency(
            @RequestParam Long fromStepId,
            @RequestParam Long toStepId,
            @RequestHeader("User-Id") Long userId) {
        recipeStepService.addStepDependency(fromStepId, toStepId, userId);
        return ResponseEntity.ok(ApiResponse.success("Step dependency added successfully", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RecipeStepDTO>> updateStep(
            @PathVariable Long id,
            @RequestParam String description,
            @RequestHeader("User-Id") Long userId) {
        RecipeStepDTO step = recipeStepService.updateStep(id, description, userId);
        return ResponseEntity.ok(ApiResponse.success("Step updated successfully", step));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStep(
            @PathVariable Long id,
            @RequestHeader("User-Id") Long userId) {
        recipeStepService.deleteStep(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Step deleted successfully", null));
    }

    @GetMapping("/recipe/{recipeId}")
    public ResponseEntity<ApiResponse<List<RecipeStepDTO>>> getRecipeSteps(@PathVariable Long recipeId) {
        List<RecipeStepDTO> steps = recipeStepService.getRecipeSteps(recipeId);
        return ResponseEntity.ok(ApiResponse.success(steps));
    }

    @GetMapping("/recipe/{recipeId}/starting")
    public ResponseEntity<ApiResponse<List<RecipeStepDTO>>> getStartingSteps(@PathVariable Long recipeId) {
        List<RecipeStepDTO> steps = recipeStepService.getStartingSteps(recipeId);
        return ResponseEntity.ok(ApiResponse.success(steps));
    }
}