package com.zerobase.cafekiosk.admin.category.dto;

import com.zerobase.cafekiosk.admin.category.entity.Category;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

  Long id;
  String categoryName;
  int sortValue;
  boolean usingYn;

  public static CategoryDto of(Category category) {
    return CategoryDto.builder()
        .id(category.getId())
        .categoryName(category.getCategoryName())
        .sortValue(category.getSortValue())
        .usingYn(category.isUsingYn())
        .build();
  }

  public static List<CategoryDto> of(List<Category> categories) {

    if (!categories.isEmpty()) {
      List<CategoryDto> categoryDtoList = new ArrayList<>();
      for (Category x : categories) {
        categoryDtoList.add(of(x));
      }
      return categoryDtoList;
    }
    return null;
  }
}
