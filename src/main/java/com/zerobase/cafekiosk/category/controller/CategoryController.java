package com.zerobase.cafekiosk.category.controller;

import com.zerobase.cafekiosk.category.dto.CategoryDto;
import com.zerobase.cafekiosk.category.model.CategoryInput;
import com.zerobase.cafekiosk.category.service.CategoryService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/admin/categories")
public class CategoryController {

  private final CategoryService categoryService;

  /**
   * 카테고리 전체 조회
   */
  @GetMapping
  public ResponseEntity<?> findAllCategories() {
    List<CategoryDto> list = categoryService.list();
    return ResponseEntity.ok(list);
  }

  /**
   * 카테고리 생성
   * 요청 : CategoryName
   */
  @PostMapping
  public ResponseEntity<?> createCategory(@RequestBody String categoryName) {
    CategoryDto categoryDto = categoryService.add(categoryName);
    return ResponseEntity.ok(categoryDto);
  }

  /**
   * 카테고리 수정
   * 매개변수 : CategoryId
   * 요청 : CategoryInput
   */
  @PutMapping("/{id}")
  public void updateCategory(@PathVariable Long id,
      @RequestBody CategoryInput request) {
    categoryService.update(id, request);
  }

  /**
   * 카테고리 삭제
   * * 매개변수 : CategoryId
   */
  @DeleteMapping("/{id}")
  public void deleteCategory(@PathVariable Long id) {
    categoryService.delete(id);
  }
}
