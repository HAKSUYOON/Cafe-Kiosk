package com.zerobase.cafekiosk.category.entity;

import com.zerobase.cafekiosk.category.model.CategoryInput;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String categoryName;

  @PositiveOrZero
  private int sortValue;

  private boolean usingYn;

  public Category buildCategory(String categoryName, int sortValue) {
    return Category.builder()
        .categoryName(categoryName)
        .usingYn(true)
        .sortValue(sortValue)
        .build();
  }

  public void setCategory(Category category, CategoryInput request) {
    category.setCategoryName(request.getCategoryName());
    category.setSortValue(request.getSortValue());
    category.setUsingYn(request.isUsingYn());
  }
}
