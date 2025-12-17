package com.kwewk.course_project.dto;

import com.kwewk.course_project.enums.MealType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealDTO {
    private Long id;
    private String name;
    private MealType mealType;
    private String photo;
    private Double rating;
    private Integer recipeCount;
}