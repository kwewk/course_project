package com.kwewk.course_project.controller;

import com.kwewk.course_project.dto.IngredientDTO;
import com.kwewk.course_project.dto.response.ApiResponse;
import com.kwewk.course_project.service.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @PostMapping("/inventory")
    public ResponseEntity<ApiResponse<IngredientDTO>> addIngredientToInventory(
            @RequestParam String name,
            @RequestParam Integer quantity,
            @RequestParam String unit,
            @RequestHeader("User-Id") Long userId) {
        IngredientDTO ingredient = ingredientService.addIngredientToInventory(name, quantity, unit, userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Ingredient added to inventory", ingredient));
    }

    @PutMapping("/inventory/{id}")
    public ResponseEntity<ApiResponse<IngredientDTO>> updateIngredientQuantity(
            @PathVariable Long id,
            @RequestParam Integer quantity,
            @RequestHeader("User-Id") Long userId) {
        IngredientDTO ingredient = ingredientService.updateIngredientQuantity(id, quantity, userId);
        return ResponseEntity.ok(ApiResponse.success("Ingredient quantity updated", ingredient));
    }

    @DeleteMapping("/inventory/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteIngredient(
            @PathVariable Long id,
            @RequestHeader("User-Id") Long userId) {
        ingredientService.deleteIngredient(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Ingredient deleted from inventory", null));
    }

    @GetMapping("/inventory")
    public ResponseEntity<ApiResponse<List<IngredientDTO>>> getUserInventory(
            @RequestHeader("User-Id") Long userId) {
        List<IngredientDTO> inventory = ingredientService.getUserInventory(userId);
        return ResponseEntity.ok(ApiResponse.success(inventory));
    }

    @GetMapping("/inventory/low-stock")
    public ResponseEntity<ApiResponse<List<IngredientDTO>>> getLowStockIngredients(
            @RequestHeader("User-Id") Long userId,
            @RequestParam(defaultValue = "10") Integer threshold) {
        List<IngredientDTO> lowStock = ingredientService.getLowStockIngredients(userId, threshold);
        return ResponseEntity.ok(ApiResponse.success(lowStock));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<IngredientDTO>>> getCommonIngredients() {
        List<IngredientDTO> ingredients = ingredientService.getCommonIngredients();
        return ResponseEntity.ok(ApiResponse.success(ingredients));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<IngredientDTO>> getIngredientById(@PathVariable Long id) {
        IngredientDTO ingredient = ingredientService.getIngredientById(id);
        return ResponseEntity.ok(ApiResponse.success(ingredient));
    }
}