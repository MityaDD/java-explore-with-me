package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.dto.CategoryDtoNew;
import ru.practicum.category.storage.CategoryRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryServiceAdmin, CategoryServicePublic {

    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public CategoryDto addCategory(CategoryDtoNew categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        log.info("Добавлена новая категория");
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(Long catId, CategoryDtoNew categoryDto) {
        Category category = categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException("Категория с айди " + catId + " не найдена"));
        category.setName(categoryDto.getName());
        log.info("Обновлена категория с id=" + catId);
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Transactional
    @Override
    public void deleteCategory(Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException("Категория с айди " + catId + " не найдена"));
        log.info("Удалена категория с id=" + catId);
        categoryRepository.deleteById(category.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        log.info("Запрошен список всех категорий");
        return categoryRepository.findAll(PageRequest.of(from / size, size)).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDto getCategory(Long catId) {
        log.info("Запрошена категория с id=" + catId);
        Category category = categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException("Категория с айди " + catId + " не найдена"));
        return CategoryMapper.toCategoryDto(category);
    }
}
