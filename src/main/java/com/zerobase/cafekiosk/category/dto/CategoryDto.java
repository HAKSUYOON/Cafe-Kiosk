package com.zerobase.cafekiosk.category.dto;

import com.zerobase.cafekiosk.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

  private Long id;
  private String categoryName;
  private int sortValue;
  private boolean usingYn;

  public static CategoryDto of(Category category) {
    return CategoryDto.builder()
        .id(category.getId())
        .categoryName(category.getCategoryName())
        .sortValue(category.getSortValue())
        .usingYn(category.isUsingYn())
        .build();
  }
}
