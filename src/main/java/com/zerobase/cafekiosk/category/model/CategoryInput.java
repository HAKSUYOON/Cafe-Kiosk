package com.zerobase.cafekiosk.category.model;

import lombok.Data;

@Data
public class CategoryInput {

  private Long id;
  private String categoryName;
  private int sortValue;
  private boolean usingYn;
}
