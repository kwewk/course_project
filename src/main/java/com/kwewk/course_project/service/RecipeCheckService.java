package com.kwewk.course_project.service;

import com.kwewk.course_project.dto.IngredientDTO;
import com.kwewk.course_project.exception.NotFoundException;
import com.kwewk.course_project.repository.RecipeIngredientRepository;
import com.kwewk.course_project.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecipeCheckService {

    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    @Transactional(readOnly = true)
    public Map<String, Object> checkRecipeAvailability(Long recipeId, Long userId) {
        if (!recipeRepository.existsById(recipeId)) {
            throw new NotFoundException("Recipe not found");
        }

        List<Object[]> missingIngredients = recipeIngredientRepository.findMissingIngredients(recipeId, userId);

        Map<String, Object> result = new HashMap<>();

        if (missingIngredients.isEmpty()) {
            result.put("possible", true);
            result.put("message", "Можливо");
            result.put("missingIngredients", new ArrayList<>());
        } else {
            result.put("possible", false);
            result.put("message", "Не вистачає інгредієнтів");

            List<IngredientDTO> missing = new ArrayList<>();
            for (Object[] row : missingIngredients) {
                IngredientDTO dto = IngredientDTO.builder()
                        .name((String) row[0])
                        .quantity(((Number) row[3]).intValue())
                        .build();
                missing.add(dto);
            }
            result.put("missingIngredients", missing);
        }

        return result;
    }
}