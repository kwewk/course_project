package com.kwewk.course_project.converter;

import com.kwewk.course_project.enums.MealType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MealTypeConverter implements AttributeConverter<MealType, String> {

    @Override
    public String convertToDatabaseColumn(MealType mealType) {
        if (mealType == null) {
            return null;
        }

        return mealType.getDisplayName();
    }

    @Override
    public MealType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }

        return switch (dbData) {
            case "Сніданок" -> MealType.BREAKFAST;
            case "Обід" -> MealType.LUNCH;
            case "Перекус" -> MealType.SNACK;
            case "Вечеря" -> MealType.DINNER;
            default -> throw new IllegalArgumentException("Unknown Meal Type in DB: " + dbData);
        };
    }
}