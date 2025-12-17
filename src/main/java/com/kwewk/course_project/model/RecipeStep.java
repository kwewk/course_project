package com.kwewk.course_project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"Recipe_step\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"Recipe_id\"", nullable = false)
    private Recipe recipe;

    @Column(name = "\"Step_description\"", columnDefinition = "TEXT")
    private String stepDescription;

    @OneToMany(mappedBy = "fromStep", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecipeEdge> outgoingEdges = new HashSet<>();

    @OneToMany(mappedBy = "toStep", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecipeEdge> incomingEdges = new HashSet<>();
}