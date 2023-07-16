package ru.practicum.category.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.category.model.Category;

@UtilityClass
public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
