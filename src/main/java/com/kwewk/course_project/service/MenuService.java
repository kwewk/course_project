package com.kwewk.course_project.service;

import com.kwewk.course_project.dto.IngredientDTO;
import com.kwewk.course_project.dto.MenuDTO;
import com.kwewk.course_project.dto.MenuMealDTO;
import com.kwewk.course_project.exception.ForbiddenException;
import com.kwewk.course_project.exception.NotFoundException;
import com.kwewk.course_project.model.*;
import com.kwewk.course_project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuMealRepository menuMealRepository;
    private final UserRepository userRepository;
    private final MealRepository mealRepository;
    private final RecipeRepository recipeRepository;

    @Transactional
    public MenuDTO createMenu(LocalDate date, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Menu menu = Menu.builder()
                .user(user)
                .date(date)
                .build();

        menu = menuRepository.save(menu);
        return convertToDTO(menu);
    }

    @Transactional
    public MenuMealDTO addMealToMenu(Long menuId, Long mealId, LocalTime time, Long userId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException("Menu not found"));

        if (!menu.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You can only edit your own menu");
        }

        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new NotFoundException("Meal not found"));

        MenuMeal menuMeal = MenuMeal.builder()
                .menu(menu)
                .meal(meal)
                .time(time)
                .build();

        menuMeal = menuMealRepository.save(menuMeal);
        return convertToMenuMealDTO(menuMeal);
    }

    @Transactional
    public MenuMealDTO updateMenuMeal(Long menuMealId, Long mealId, LocalTime time, Long userId) {
        MenuMeal menuMeal = menuMealRepository.findById(menuMealId)
                .orElseThrow(() -> new NotFoundException("Menu meal not found"));

        if (!menuMeal.getMenu().getUser().getId().equals(userId)) {
            throw new ForbiddenException("You can only edit your own menu");
        }

        if (mealId != null) {
            Meal meal = mealRepository.findById(mealId)
                    .orElseThrow(() -> new NotFoundException("Meal not found"));
            menuMeal.setMeal(meal);
        }

        if (time != null) {
            menuMeal.setTime(time);
        }

        menuMeal = menuMealRepository.save(menuMeal);
        return convertToMenuMealDTO(menuMeal);
    }

    @Transactional(readOnly = true)
    public List<IngredientDTO> generateShoppingList(Long menuId, Long userId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException("Menu not found"));

        if (!menu.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You can only view your own menu");
        }

        List<Object[]> missingIngredients = menuMealRepository.generateShoppingList(menuId, userId);

        List<IngredientDTO> shoppingList = new ArrayList<>();
        for (Object[] row : missingIngredients) {
            IngredientDTO dto = IngredientDTO.builder()
                    .name((String) row[0])
                    .quantity(((Number) row[4]).intValue())
                    .unit((String) row[2])
                    .build();
            shoppingList.add(dto);
        }

        return shoppingList;
    }

    @Transactional(readOnly = true)
    public MenuDTO getMenuByDate(LocalDate date, Long userId) {
        Menu menu = menuRepository.findByUserIdAndDate(userId, date)
                .orElseThrow(() -> new NotFoundException("Menu not found for date: " + date));
        return convertToDTO(menu);
    }

    @Transactional(readOnly = true)
    public List<MenuDTO> getMenusByDateRange(LocalDate startDate, LocalDate endDate, Long userId) {
        return menuRepository.findByUserIdAndDateBetween(userId, startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getMostFrequentMeals(LocalDate startDate, LocalDate endDate, Long userId, int limit) {
        List<Object[]> results = menuRepository.findMostFrequentMeals(userId, startDate, endDate, limit);

        Map<String, Long> frequentMeals = new HashMap<>();
        for (Object[] row : results) {
            String mealName = (String) row[0];
            Long frequency = ((Number) row[1]).longValue();
            frequentMeals.put(mealName, frequency);
        }

        return frequentMeals;
    }

    @Transactional(readOnly = true)
    public List<IngredientDTO> getShoppingList(Long menuId, Long userId) {
        return generateShoppingList(menuId, userId);
    }

    @Transactional
    public void deleteMenu(Long menuId, Long userId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException("Menu not found"));

        if (!menu.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You can only delete your own menu");
        }

        menuRepository.delete(menu);
    }

    @Transactional(readOnly = true)
    public Integer calculateMenuCalories(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException("Menu not found"));

        Integer totalCalories = 0;

        for (MenuMeal menuMeal : menu.getMenuMeals()) {
            Meal meal = menuMeal.getMeal();

            List<Recipe> recipes = recipeRepository.findByMealId(meal.getId());

            if (!recipes.isEmpty()) {
                Recipe recipe = recipes.get(0);

                for (RecipeIngredient ri : recipe.getIngredients()) {
                    Ingredient ingredient = ri.getIngredient();
                    if (ingredient.getCalorieContent() != null) {
                        totalCalories += (ingredient.getCalorieContent() * ri.getQuantity()) / 100;
                    }
                }
            }
        }

        return totalCalories;
    }

    private MenuDTO convertToDTO(Menu menu) {
        List<MenuMealDTO> meals = menu.getMenuMeals().stream()
                .map(this::convertToMenuMealDTO)
                .collect(Collectors.toList());

        Integer totalCalories = calculateMenuCalories(menu.getId());

        return MenuDTO.builder()
                .id(menu.getId())
                .userId(menu.getUser().getId())
                .date(menu.getDate())
                .meals(meals)
                .totalCalories(totalCalories)
                .build();
    }

    private MenuMealDTO convertToMenuMealDTO(MenuMeal menuMeal) {
        return MenuMealDTO.builder()
                .id(menuMeal.getId())
                .mealId(menuMeal.getMeal().getId())
                .mealName(menuMeal.getMeal().getName())
                .time(menuMeal.getTime())
                .build();
    }
}