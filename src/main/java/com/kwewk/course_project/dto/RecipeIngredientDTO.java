package com.kwewk.course_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeIngredientDTO {
    private Long id;
    private Long ingredientId;
    private String ingredientName;
    private Integer quantity;
    private String unit;
}