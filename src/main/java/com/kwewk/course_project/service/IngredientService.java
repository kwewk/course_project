package com.kwewk.course_project.service;

import com.kwewk.course_project.dto.IngredientDTO;
import com.kwewk.course_project.exception.ForbiddenException;
import com.kwewk.course_project.exception.NotFoundException;
import com.kwewk.course_project.model.Ingredient;
import com.kwewk.course_project.model.User;
import com.kwewk.course_project.repository.IngredientRepository;
import com.kwewk.course_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;

    @Transactional
    public IngredientDTO addIngredientToInventory(String name, Integer quantity, String unit, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Ingredient ingredient = Ingredient.builder()
                .name(name)
                .quantity(quantity)
                .unit(unit)
                .user(user)
                .build();

        ingredient = ingredientRepository.save(ingredient);
        return convertToDTO(ingredient);
    }

    @Transactional
    public IngredientDTO updateIngredientQuantity(Long ingredientId, Integer quantity, Long userId) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new NotFoundException("Ingredient not found"));

        if (ingredient.getUser() == null || !ingredient.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You can only edit your own inventory");
        }

        ingredient.setQuantity(quantity);
        ingredient = ingredientRepository.save(ingredient);
        return convertToDTO(ingredient);
    }

    @Transactional
    public void deleteIngredient(Long ingredientId, Long userId) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new NotFoundException("Ingredient not found"));

        if (ingredient.getUser() == null || !ingredient.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You can only delete your own inventory");
        }

        ingredientRepository.delete(ingredient);
    }

    @Transactional(readOnly = true)
    public List<IngredientDTO> getUserInventory(Long userId) {
        return ingredientRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<IngredientDTO> getLowStockIngredients(Long userId, Integer threshold) {
        return ingredientRepository.findLowStockIngredients(userId, threshold).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<IngredientDTO> getCommonIngredients() {
        return ingredientRepository.findCommonIngredients().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public IngredientDTO getIngredientById(Long id) {
        Ingredient ingredient = ingredientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ingredient not found"));
        return convertToDTO(ingredient);
    }

    private IngredientDTO convertToDTO(Ingredient ingredient) {
        return IngredientDTO.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .calorieContent(ingredient.getCalorieContent())
                .quantity(ingredient.getQuantity())
                .unit(ingredient.getUnit())
                .userId(ingredient.getUser() != null ? ingredient.getUser().getId() : null)
                .build();
    }
}