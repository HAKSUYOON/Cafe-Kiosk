package com.zerobase.cafekiosk.category.service;

import com.zerobase.cafekiosk.category.dto.CategoryDto;
import com.zerobase.cafekiosk.category.entity.Category;
import com.zerobase.cafekiosk.category.model.CategoryInput;
import com.zerobase.cafekiosk.category.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;

  public Sort getSortBySortValue() {
    return Sort.by(Direction.ASC, "sortValue");
  }

  @Override
  public List<CategoryDto> list() {

    List<Category> categories = categoryRepository.findAll();
    return CategoryDto.of(categories);
  }

  @Override
  public List<CategoryDto> frontList() {
    List<Category> categories = categoryRepository.findByUsingYnTrue();
    return CategoryDto.of(categories);
  }

  @Override
  public CategoryDto add(String categoryName) {

    if (categoryRepository.findByCategoryName(categoryName).isPresent()) {
      throw new RuntimeException("이미 존재하는 카테고리명 입니다.");
    }

    Category category = Category.builder()
        .categoryName(categoryName)
        .usingYn(true)
        .sortValue(0)
        .build();
    categoryRepository.save(category);

    return CategoryDto.of(category);
  }

  @Override
  public void delete(Long id) {

    if (categoryRepository.findById(id).isEmpty()) {
      throw new RuntimeException("존재하지 않는 카테고리입니다.");
    }

    categoryRepository.deleteById(id);
  }

  @Override
  public void update(Long id, CategoryInput parameter) {

    Category category = categoryRepository.findById(id).
        orElseThrow(() -> new RuntimeException("해당 카테고리가 존재하지 않습니다."));

    category.setCategoryName(parameter.getCategoryName());
    category.setSortValue(parameter.getSortValue());
    category.setUsingYn(parameter.isUsingYn());
    categoryRepository.save(category);
  }
}