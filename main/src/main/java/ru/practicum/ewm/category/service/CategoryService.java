package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryDtoShort;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDtoShort categoryDtoShort);

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategory(Long catId);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(Long catId, CategoryDto categoryDto);

}