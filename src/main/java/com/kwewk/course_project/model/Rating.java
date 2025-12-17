package com.kwewk.course_project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "\"Rating\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"User_id\"", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"Meal_id\"", nullable = false)
    private Meal meal;

    @Column(name = "\"Rating\"", nullable = false)
    private Double rating;

    @Column(name = "\"Text\"", columnDefinition = "TEXT")
    private String text;

    @Column(name = "\"Date\"", nullable = false)
    private LocalDate date;
}