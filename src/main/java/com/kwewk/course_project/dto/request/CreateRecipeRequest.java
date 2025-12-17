package com.kwewk.course_project.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRecipeRequest {

    @NotNull(message = "Description is required")
    private String description;

    @Positive(message = "Cooking time must be positive")
    private Integer cookingTime;

    private String photo;

    @NotNull(message = "Meal ID is required")
    private Long mealId;

    private List<RecipeIngredientRequest> ingredients;
}