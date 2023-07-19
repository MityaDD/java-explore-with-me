package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;

import java.util.List;

public interface CategoryServicePublic {
    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategory(Long catId);
}
