package com.zerobase.cafekiosk.category.service;

import com.zerobase.cafekiosk.category.dto.CategoryDto;
import com.zerobase.cafekiosk.category.model.CategoryInput;
import java.util.List;

public interface CategoryService {

  List<CategoryDto> list();

  CategoryDto add(String categoryName);

  void delete(Long id);

  void update(Long id, CategoryInput parameter);

  List<CategoryDto> frontList();
}
