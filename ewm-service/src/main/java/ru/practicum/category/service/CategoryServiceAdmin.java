package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryDtoNew;

public interface CategoryServiceAdmin {
    CategoryDto addCategory(CategoryDtoNew categoryDto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(Long catId, CategoryDtoNew categoryDto);
}
