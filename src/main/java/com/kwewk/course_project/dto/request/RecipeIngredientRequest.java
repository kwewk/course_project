package com.kwewk.course_project.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeIngredientRequest {

    @NotNull(message = "Ingredient ID is required")
    private Long ingredientId;

    @Positive(message = "Quantity must be positive")
    private Integer quantity;
}