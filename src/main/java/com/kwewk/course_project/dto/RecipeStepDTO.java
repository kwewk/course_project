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
public class RecipeStepDTO {
    private Long id;
    private String stepDescription;
    private List<Long> nextStepIds;
}