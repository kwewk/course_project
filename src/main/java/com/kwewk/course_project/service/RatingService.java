package com.kwewk.course_project.service;

import com.kwewk.course_project.dto.RatingDTO;
import com.kwewk.course_project.dto.request.CreateRatingRequest;
import com.kwewk.course_project.exception.BadRequestException;
import com.kwewk.course_project.exception.ForbiddenException;
import com.kwewk.course_project.exception.NotFoundException;
import com.kwewk.course_project.model.Meal;
import com.kwewk.course_project.model.Rating;
import com.kwewk.course_project.model.User;
import com.kwewk.course_project.repository.MealRepository;
import com.kwewk.course_project.repository.RatingRepository;
import com.kwewk.course_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final MealRepository mealRepository;

    @Transactional
    public RatingDTO createRating(CreateRatingRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Meal meal = mealRepository.findById(request.getMealId())
                .orElseThrow(() -> new NotFoundException("Meal not found"));

        if (ratingRepository.hasUserRatedMeal(request.getMealId(), userId)) {
            throw new BadRequestException("You have already rated this meal");
        }

        Rating rating = Rating.builder()
                .user(user)
                .meal(meal)
                .rating(request.getRating())
                .text(request.getText())
                .date(LocalDate.now())
                .build();

        rating = ratingRepository.save(rating);

        updateMealAverageRating(meal.getId());

        return convertToDTO(rating);
    }

    @Transactional
    public RatingDTO addComment(Long mealId, String text, Long userId) {
        CreateRatingRequest request = CreateRatingRequest.builder()
                .mealId(mealId)
                .rating(0.0)
                .text(text)
                .build();

        return createRating(request, userId);
    }

    @Transactional
    public RatingDTO updateRating(Long ratingId, String text, Double newRating, Long userId) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new NotFoundException("Rating not found"));

        if (!rating.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You can only edit your own ratings");
        }

        if (text != null) {
            rating.setText(text);
        }
        if (newRating != null) {
            rating.setRating(newRating);
            updateMealAverageRating(rating.getMeal().getId());
        }

        rating = ratingRepository.save(rating);
        return convertToDTO(rating);
    }

    @Transactional(readOnly = true)
    public List<RatingDTO> getMealRatings(Long mealId) {
        return ratingRepository.findByMealId(mealId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteRating(Long ratingId, Long userId) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new NotFoundException("Rating not found"));

        if (!rating.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You can only delete your own ratings");
        }

        Long mealId = rating.getMeal().getId();
        ratingRepository.delete(rating);

        updateMealAverageRating(mealId);
    }

    private void updateMealAverageRating(Long mealId) {
        Double avgRating = ratingRepository.calculateAverageRating(mealId);
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new NotFoundException("Meal not found"));

        meal.setRating(avgRating);
        mealRepository.save(meal);
    }

    private RatingDTO convertToDTO(Rating rating) {
        return RatingDTO.builder()
                .id(rating.getId())
                .userId(rating.getUser().getId())
                .userName(rating.getUser().getName())
                .mealId(rating.getMeal().getId())
                .mealName(rating.getMeal().getName())
                .rating(rating.getRating())
                .text(rating.getText())
                .date(rating.getDate())
                .build();
    }
}