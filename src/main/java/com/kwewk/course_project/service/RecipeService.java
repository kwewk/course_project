package com.kwewk.course_project.service;

import com.kwewk.course_project.dto.RecipeDTO;
import com.kwewk.course_project.dto.RecipeIngredientDTO;
import com.kwewk.course_project.dto.RecipeStepDTO;
import com.kwewk.course_project.dto.request.CreateRecipeRequest;
import com.kwewk.course_project.dto.request.RecipeIngredientRequest;
import com.kwewk.course_project.exception.ForbiddenException;
import com.kwewk.course_project.exception.NotFoundException;
import com.kwewk.course_project.model.*;
import com.kwewk.course_project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final MealRepository mealRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeStepRepository recipeStepRepository;

    @Transactional
    public RecipeDTO createRecipe(CreateRecipeRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Meal meal = mealRepository.findById(request.getMealId())
                .orElseThrow(() -> new NotFoundException("Meal not found"));

        Recipe recipe = Recipe.builder()
                .description(request.getDescription())
                .cookingTime(request.getCookingTime())
                .photo(request.getPhoto())
                .user(user)
                .meal(meal)
                .build();

        recipe = recipeRepository.save(recipe);

        if (request.getIngredients() != null) {
            for (RecipeIngredientRequest ingredientReq : request.getIngredients()) {
                Ingredient ingredient = ingredientRepository.findById(ingredientReq.getIngredientId())
                        .orElseThrow(() -> new NotFoundException("Ingredient not found"));

                RecipeIngredient recipeIngredient = RecipeIngredient.builder()
                        .recipe(recipe)
                        .ingredient(ingredient)
                        .quantity(ingredientReq.getQuantity())
                        .build();

                recipeIngredientRepository.save(recipeIngredient);
            }
        }

        return convertToDTO(recipeRepository.findById(recipe.getId()).get());
    }

    @Transactional
    public RecipeDTO updateRecipe(Long recipeId, CreateRecipeRequest request, Long userId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new NotFoundException("Recipe not found"));

        if (!recipe.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You can only edit your own recipes");
        }

        recipe.setDescription(request.getDescription());
        recipe.setCookingTime(request.getCookingTime());
        recipe.setPhoto(request.getPhoto());

        if (request.getMealId() != null) {
            Meal meal = mealRepository.findById(request.getMealId())
                    .orElseThrow(() -> new NotFoundException("Meal not found"));
            recipe.setMeal(meal);
        }

        recipe = recipeRepository.save(recipe);
        return convertToDTO(recipe);
    }

    @Transactional
    public void deleteRecipe(Long recipeId, Long userId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new NotFoundException("Recipe not found"));

        if (!recipe.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You can only delete your own recipes");
        }

        recipeRepository.delete(recipe);
    }

    @Transactional(readOnly = true)
    public List<RecipeDTO> searchRecipesByIngredient(String ingredientName) {
        return recipeRepository.searchByIngredientName(ingredientName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecipeDTO> searchRecipesByMealName(String mealName) {
        return recipeRepository.searchByMealName(mealName).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RecipeDTO getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Recipe not found"));
        return convertToDTO(recipe);
    }

    @Transactional(readOnly = true)
    public List<RecipeDTO> getUserRecipes(Long userId) {
        return recipeRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecipeDTO> getMealRecipes(Long mealId) {
        return recipeRepository.findByMealId(mealId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addToFavorites(Long recipeId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new NotFoundException("Recipe not found"));

        user.getFavoriteRecipes().add(recipe);
        userRepository.save(user);
    }

    @Transactional
    public void removeFromFavorites(Long recipeId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new NotFoundException("Recipe not found"));

        user.getFavoriteRecipes().remove(recipe);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<RecipeDTO> getFavoriteRecipes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return user.getFavoriteRecipes().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private RecipeDTO convertToDTO(Recipe recipe) {
        List<RecipeIngredientDTO> ingredients = recipe.getIngredients().stream()
                .map(ri -> RecipeIngredientDTO.builder()
                        .id(ri.getId())
                        .ingredientId(ri.getIngredient().getId())
                        .ingredientName(ri.getIngredient().getName())
                        .quantity(ri.getQuantity())
                        .unit(ri.getIngredient().getUnit())
                        .build())
                .collect(Collectors.toList());

        List<RecipeStepDTO> steps = recipe.getSteps().stream()
                .map(step -> RecipeStepDTO.builder()
                        .id(step.getId())
                        .stepDescription(step.getStepDescription())
                        .nextStepIds(step.getOutgoingEdges().stream()
                                .map(edge -> edge.getToStep().getId())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        return RecipeDTO.builder()
                .id(recipe.getId())
                .description(recipe.getDescription())
                .cookingTime(recipe.getCookingTime())
                .photo(recipe.getPhoto())
                .userId(recipe.getUser().getId())
                .userName(recipe.getUser().getName())
                .mealId(recipe.getMeal().getId())
                .mealName(recipe.getMeal().getName())
                .ingredients(ingredients)
                .steps(steps)
                .build();
    }
}