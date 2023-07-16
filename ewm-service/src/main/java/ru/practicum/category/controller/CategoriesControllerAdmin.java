package ru.practicum.category.controller;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryDtoNew;
import ru.practicum.category.service.CategoryServiceImpl;

import javax.validation.Valid;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoriesControllerAdmin {
    final CategoryServiceImpl categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CategoryDto addCategory(@RequestBody @Valid CategoryDtoNew categoryDto) {
        return categoryService.addCategory(categoryDto);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    CategoryDto updateCategory(@PathVariable Long catId, @RequestBody @Valid CategoryDtoNew categoryDto) {
        return categoryService.updateCategory(catId, categoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategory(catId);
    }
}
