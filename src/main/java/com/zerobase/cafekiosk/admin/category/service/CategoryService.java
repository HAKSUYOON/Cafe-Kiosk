package com.zerobase.cafekiosk.admin.category.service;

import com.zerobase.cafekiosk.admin.category.dto.CategoryDto;
import com.zerobase.cafekiosk.admin.category.model.CategoryInput;
import java.util.List;

public interface CategoryService {

  List<CategoryDto> list();

  boolean add(String categoryName);

  boolean delete(Long id);

  boolean update(CategoryInput parameter);
}
