package com.kwewk.course_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDTO {
    private Long id;
    private String description;
    private Integer cookingTime;
    private String photo;
    private Long userId;
    private String userName;
    private Long mealId;
    private String mealName;
    private List<RecipeIngredientDTO> ingredients;
    private List<RecipeStepDTO> steps;
}