package com.kwewk.course_project.enums;

public enum MealType {
    BREAKFAST("Сніданок"),
    LUNCH("Обід"),
    SNACK("Перекус"),
    DINNER("Вечеря");

    private final String displayName;

    MealType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}