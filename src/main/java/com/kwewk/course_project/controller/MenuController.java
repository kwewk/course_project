package com.kwewk.course_project.controller;

import com.kwewk.course_project.dto.IngredientDTO;
import com.kwewk.course_project.dto.MenuDTO;
import com.kwewk.course_project.dto.MenuMealDTO;
import com.kwewk.course_project.dto.response.ApiResponse;
import com.kwewk.course_project.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<ApiResponse<MenuDTO>> createMenu(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestHeader("User-Id") Long userId) {
        MenuDTO menu = menuService.createMenu(date, userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Menu created successfully", menu));
    }

    @PostMapping("/{menuId}/meals")
    public ResponseEntity<ApiResponse<MenuMealDTO>> addMealToMenu(
            @PathVariable Long menuId,
            @RequestParam Long mealId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
            @RequestHeader("User-Id") Long userId) {
        MenuMealDTO menuMeal = menuService.addMealToMenu(menuId, mealId, time, userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Meal added to menu successfully", menuMeal));
    }

    @PutMapping("/meals/{menuMealId}")
    public ResponseEntity<ApiResponse<MenuMealDTO>> updateMenuMeal(
            @PathVariable Long menuMealId,
            @RequestParam(required = false) Long mealId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime time,
            @RequestHeader("User-Id") Long userId) {
        MenuMealDTO menuMeal = menuService.updateMenuMeal(menuMealId, mealId, time, userId);
        return ResponseEntity.ok(ApiResponse.success("Menu meal updated successfully", menuMeal));
    }

    @GetMapping("/{menuId}/shopping-list")
    public ResponseEntity<ApiResponse<List<IngredientDTO>>> getShoppingList(
            @PathVariable Long menuId,
            @RequestHeader("User-Id") Long userId) {
        List<IngredientDTO> shoppingList = menuService.getShoppingList(menuId, userId);
        return ResponseEntity.ok(ApiResponse.success(shoppingList));
    }

    @GetMapping("/by-date")
    public ResponseEntity<ApiResponse<MenuDTO>> getMenuByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestHeader("User-Id") Long userId) {
        MenuDTO menu = menuService.getMenuByDate(date, userId);
        return ResponseEntity.ok(ApiResponse.success(menu));
    }

    @GetMapping("/by-date-range")
    public ResponseEntity<ApiResponse<List<MenuDTO>>> getMenusByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestHeader("User-Id") Long userId) {
        List<MenuDTO> menus = menuService.getMenusByDateRange(startDate, endDate, userId);
        return ResponseEntity.ok(ApiResponse.success(menus));
    }

    @GetMapping("/frequent-meals")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getMostFrequentMeals(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestHeader("User-Id") Long userId,
            @RequestParam(defaultValue = "10") int limit) {
        Map<String, Long> frequentMeals = menuService.getMostFrequentMeals(startDate, endDate, userId, limit);
        return ResponseEntity.ok(ApiResponse.success(frequentMeals));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMenu(
            @PathVariable Long id,
            @RequestHeader("User-Id") Long userId) {
        menuService.deleteMenu(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Menu deleted successfully", null));
    }
}