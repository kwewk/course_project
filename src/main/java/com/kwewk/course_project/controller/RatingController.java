package com.kwewk.course_project.controller;

import com.kwewk.course_project.dto.RatingDTO;
import com.kwewk.course_project.dto.request.CreateRatingRequest;
import com.kwewk.course_project.dto.response.ApiResponse;
import com.kwewk.course_project.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @PostMapping
    public ResponseEntity<ApiResponse<RatingDTO>> createRating(
            @Valid @RequestBody CreateRatingRequest request,
            @RequestHeader("User-Id") Long userId) {
        RatingDTO rating = ratingService.createRating(request, userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Rating created successfully", rating));
    }

    @PostMapping("/comment")
    public ResponseEntity<ApiResponse<RatingDTO>> addComment(
            @RequestParam Long mealId,
            @RequestParam String text,
            @RequestHeader("User-Id") Long userId) {
        RatingDTO rating = ratingService.addComment(mealId, text, userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Comment added successfully", rating));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RatingDTO>> updateRating(
            @PathVariable Long id,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) Double rating,
            @RequestHeader("User-Id") Long userId) {
        RatingDTO updatedRating = ratingService.updateRating(id, text, rating, userId);
        return ResponseEntity.ok(ApiResponse.success("Rating updated successfully", updatedRating));
    }

    @GetMapping("/meal/{mealId}")
    public ResponseEntity<ApiResponse<List<RatingDTO>>> getMealRatings(@PathVariable Long mealId) {
        List<RatingDTO> ratings = ratingService.getMealRatings(mealId);
        return ResponseEntity.ok(ApiResponse.success(ratings));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRating(
            @PathVariable Long id,
            @RequestHeader("User-Id") Long userId) {
        ratingService.deleteRating(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Rating deleted successfully", null));
    }
}