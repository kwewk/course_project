package com.kwewk.course_project.service;

import com.kwewk.course_project.dto.RecipeStepDTO;
import com.kwewk.course_project.exception.ForbiddenException;
import com.kwewk.course_project.exception.NotFoundException;
import com.kwewk.course_project.model.Recipe;
import com.kwewk.course_project.model.RecipeEdge;
import com.kwewk.course_project.model.RecipeStep;
import com.kwewk.course_project.repository.RecipeEdgeRepository;
import com.kwewk.course_project.repository.RecipeRepository;
import com.kwewk.course_project.repository.RecipeStepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeStepService {

    private final RecipeStepRepository recipeStepRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeEdgeRepository recipeEdgeRepository;

    @Transactional
    public RecipeStepDTO addStep(Long recipeId, String description, Long userId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new NotFoundException("Recipe not found"));

        if (!recipe.getUser().getId().equals(userId)) {
            throw new ForbiddenException("You can only edit your own recipes");
        }

        RecipeStep step = RecipeStep.builder()
                .recipe(recipe)
                .stepDescription(description)
                .build();

        step = recipeStepRepository.save(step);
        return convertToDTO(step);
    }

    @Transactional
    public void addStepDependency(Long fromStepId, Long toStepId, Long userId) {
        RecipeStep fromStep = recipeStepRepository.findById(fromStepId)
                .orElseThrow(() -> new NotFoundException("From step not found"));

        RecipeStep toStep = recipeStepRepository.findById(toStepId)
                .orElseThrow(() -> new NotFoundException("To step not found"));

        if (!fromStep.getRecipe().getUser().getId().equals(userId)) {
            throw new ForbiddenException("You can only edit your own recipes");
        }

        RecipeEdge edge = RecipeEdge.builder()
                .fromStep(fromStep)
                .toStep(toStep)
                .build();

        recipeEdgeRepository.save(edge);
    }

    @Transactional
    public RecipeStepDTO updateStep(Long stepId, String description, Long userId) {
        RecipeStep step = recipeStepRepository.findById(stepId)
                .orElseThrow(() -> new NotFoundException("Step not found"));

        if (!step.getRecipe().getUser().getId().equals(userId)) {
            throw new ForbiddenException("You can only edit your own recipes");
        }

        step.setStepDescription(description);
        step = recipeStepRepository.save(step);
        return convertToDTO(step);
    }

    @Transactional
    public void deleteStep(Long stepId, Long userId) {
        RecipeStep step = recipeStepRepository.findById(stepId)
                .orElseThrow(() -> new NotFoundException("Step not found"));

        if (!step.getRecipe().getUser().getId().equals(userId)) {
            throw new ForbiddenException("You can only edit your own recipes");
        }

        recipeEdgeRepository.deleteByFromStepIdOrToStepId(stepId, stepId);

        recipeStepRepository.delete(step);
    }

    @Transactional(readOnly = true)
    public List<RecipeStepDTO> getRecipeSteps(Long recipeId) {
        return recipeStepRepository.findByRecipeId(recipeId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecipeStepDTO> getStartingSteps(Long recipeId) {
        return recipeStepRepository.findStartingSteps(recipeId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private RecipeStepDTO convertToDTO(RecipeStep step) {
        List<Long> nextStepIds = step.getOutgoingEdges().stream()
                .map(edge -> edge.getToStep().getId())
                .collect(Collectors.toList());

        return RecipeStepDTO.builder()
                .id(step.getId())
                .stepDescription(step.getStepDescription())
                .nextStepIds(nextStepIds)
                .build();
    }
}