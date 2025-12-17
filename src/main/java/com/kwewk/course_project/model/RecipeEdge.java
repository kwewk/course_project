package com.kwewk.course_project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"Recipe_edge\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeEdge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "\"ID\"")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"FromStep_id\"", nullable = false)
    private RecipeStep fromStep;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"ToStep_id\"", nullable = false)
    private RecipeStep toStep;
}