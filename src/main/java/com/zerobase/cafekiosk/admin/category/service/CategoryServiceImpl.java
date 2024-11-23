package com.zerobase.cafekiosk.admin.category.service;

import com.zerobase.cafekiosk.admin.category.dto.CategoryDto;
import com.zerobase.cafekiosk.admin.category.entity.Category;
import com.zerobase.cafekiosk.admin.category.model.CategoryInput;
import com.zerobase.cafekiosk.admin.category.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
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

    List<Category> categories = categoryRepository.findAll(getSortBySortValue());
    return CategoryDto.of(categories);
  }

  @Override
  public boolean add(String categoryName) {

    Optional<Category> optionalCategory = categoryRepository.findByCategoryName(categoryName);
    if (optionalCategory.isPresent()) {
      return false;
    }

    Category category = Category.builder()
        .categoryName(categoryName)
        .usingYn(true)
        .sortValue(0)
        .build();
    categoryRepository.save(category);

    return true;
  }

  @Override
  public boolean delete(Long id) {

    categoryRepository.deleteById(id);

    return true;
  }

  @Override
  public boolean update(CategoryInput parameter) {

    Optional<Category> optionalCategory = categoryRepository.findById(parameter.getId());
    if (optionalCategory.isPresent()) {
      Category category = optionalCategory.get();
      category.setCategoryName(parameter.getCategoryName());
      category.setSortValue(parameter.getSortValue());
      category.setUsingYn(parameter.isUsingYn());
      categoryRepository.save(category);
    }
    return true;
  }
}
