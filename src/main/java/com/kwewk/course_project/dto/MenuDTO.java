package com.kwewk.course_project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDTO {
    private Long id;
    private Long userId;
    private LocalDate date;
    private List<MenuMealDTO> meals;
    private Integer totalCalories;
}