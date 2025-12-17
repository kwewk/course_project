package com.kwewk.course_project.service;

import com.kwewk.course_project.dto.MealDTO;
import com.kwewk.course_project.enums.MealType;
import com.kwewk.course_project.exception.NotFoundException;
import com.kwewk.course_project.model.Meal;
import com.kwewk.course_project.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;

    @Transactional(readOnly = true)
    public List<MealDTO> searchMealsByName(String searchTerm) {
        return mealRepository.searchByName(searchTerm).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MealDTO getMealById(Long id) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Meal not found with id: " + id));
        return convertToDTO(meal);
    }

    @Transactional(readOnly = true)
    public List<MealDTO> getAllMeals() {
        return mealRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MealDTO> getMealsOrderedByCalories() {
        return mealRepository.findAllOrderByCalories().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MealDTO> getMealsOrderedByRating() {
        return mealRepository.findAllOrderByRating().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MealDTO> getMealsByType(MealType mealType) {
        return mealRepository.findByMealType(mealType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MealDTO convertToDTO(Meal meal) {
        return MealDTO.builder()
                .id(meal.getId())
                .name(meal.getName())
                .mealType(meal.getMealType())
                .photo(meal.getPhoto())
                .rating(meal.getRating())
                .recipeCount(meal.getRecipes() != null ? meal.getRecipes().size() : 0)
                .build();
    }
}