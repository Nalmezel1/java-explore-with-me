package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryDtoShort;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.exceptions.CategoryIsNotEmptyException;
import ru.practicum.ewm.exceptions.EntityNotFoundException;
import ru.practicum.ewm.exceptions.NameAlreadyExistException;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.repository.EventRepository;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDtoShort categoryDtoShort) {
        checkCategoryName(categoryDtoShort.getName());
        return categoryMapper.toCategoryDto(categoryRepository.save(categoryMapper.toCategory(categoryDtoShort)));
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        return categoryMapper.toCategoryDtoList(categoryRepository.findAll(page).toList());
    }

    @Override
    public CategoryDto getCategory(Long categoryId) {
        checkCategoryId(categoryId);
        Category category = categoryRepository.getReferenceById(categoryId);
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long categoryId) {
        if (eventRepository.existsByCategoryId(categoryId)) {
            throw new CategoryIsNotEmptyException("Категория не пуста");
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto) {
        checkCategoryId(categoryId);
        if(!categoryRepository.getReferenceById(categoryId).getName().equals(categoryDto.getName())){
            checkCategoryName(categoryDto.getName());
        }
        Category category = categoryRepository.getReferenceById(categoryId);
        category.setName(categoryDto.getName());
        return categoryMapper.toCategoryDto(category);
    }

    public void checkCategoryId(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new EntityNotFoundException("атекория с ID " + categoryId + " не найдена.");
        }
    }

    public void checkCategoryName(String categoryName) {
        if (categoryRepository.existsByName(categoryName)) {
            throw new NameAlreadyExistException("атекория с именем " + categoryName + " уже существует.");
        }
    }
}