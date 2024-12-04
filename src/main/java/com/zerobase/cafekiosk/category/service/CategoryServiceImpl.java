package com.zerobase.cafekiosk.category.service;

import com.zerobase.cafekiosk.category.dto.CategoryDto;
import com.zerobase.cafekiosk.category.entity.Category;
import com.zerobase.cafekiosk.category.model.CategoryInput;
import com.zerobase.cafekiosk.category.repository.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  public int defaultSortValue() {
    return categoryRepository.findFirstByOrderByIdDesc()
        .map(category -> category.getSortValue() + 1).orElse(0);
  }

  public Sort getSortBySortValue() {
    return Sort.by(Direction.ASC, "sortValue");
  }

  @Override
  public List<CategoryDto> list() {

    List<Category> categories = categoryRepository.findAll(getSortBySortValue());

    return categories.stream().map(CategoryDto::of).collect(Collectors.toList());
  }

  @Override
  public List<CategoryDto> frontList() {
    List<Category> categories = categoryRepository.findByUsingYnTrueOrderBySortValue();

    return categories.stream().map(CategoryDto::of).collect(Collectors.toList());
  }

  @Override
  public CategoryDto add(String categoryName) {

    if (categoryRepository.existsByCategoryName(categoryName)) {
      throw new RuntimeException("이미 존재하는 카테고리명 입니다.");
    }

    Category category = new Category().buildCategory(categoryName, defaultSortValue());
    categoryRepository.save(category);

    return CategoryDto.of(category);
  }

  @Override
  public void delete(Long id) {

    if (!categoryRepository.existsById(id)) {
      throw new RuntimeException("존재하지 않는 카테고리입니다.");
    }

    categoryRepository.deleteById(id);
  }

  @Override
  public void update(Long id, CategoryInput request) {

    Category category = categoryRepository.findById(id).
        orElseThrow(() -> new RuntimeException("해당 카테고리가 존재하지 않습니다."));

    category.setCategory(category, request);
    categoryRepository.save(category);
  }
}