package com.kwewk.course_project.model;

import com.kwewk.course_project.converter.MealTypeConverter;
import com.kwewk.course_project.enums.MealType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"Meal\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Long id;

    @Column(name = "\"Name\"", nullable = false)
    private String name;

    @Convert(converter = MealTypeConverter.class)  // ← ДОБАВЬ ЭТО!
    @Column(name = "\"Meal_name\"")
    private MealType mealType;

    @Type(JsonType.class)
    @Column(name = "\"Photo\"", columnDefinition = "jsonb")
    private String photo;

    @Column(name = "\"Rating\"")
    private Double rating;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Recipe> recipes = new HashSet<>();

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Rating> ratings = new HashSet<>();

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL)
    private Set<MenuMeal> menuMeals = new HashSet<>();
}