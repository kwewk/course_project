package com.kwewk.course_project.model;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"Recipe\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Long id;

    @Column(name = "\"Description\"", columnDefinition = "TEXT")
    private String description;

    @Column(name = "\"CookingTime\"")
    private Integer cookingTime;

    @Type(JsonBinaryType.class)
    @Column(name = "\"Photo\"", columnDefinition = "jsonb")
    private String photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"User_id\"", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"Meal_id\"", nullable = false)
    private Meal meal;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecipeIngredient> ingredients = new HashSet<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecipeStep> steps = new HashSet<>();

    @ManyToMany(mappedBy = "favoriteRecipes")
    private Set<User> favoritedByUsers = new HashSet<>();
}