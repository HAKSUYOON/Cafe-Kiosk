package com.zerobase.cafekiosk.main.controller;

import com.zerobase.cafekiosk.beverage.dto.BeverageDto;
import com.zerobase.cafekiosk.beverage.service.BeverageService;
import com.zerobase.cafekiosk.category.dto.CategoryDto;
import com.zerobase.cafekiosk.category.service.CategoryService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/main")
public class MainController {

  private final CategoryService categoryService;
  private final BeverageService beverageService;

  /**
   * 사용자 카테고리 조회
   */
  @GetMapping("/categories")
  public ResponseEntity<?> frontCategories() {

    List<CategoryDto> list = categoryService.frontList();

    return ResponseEntity.ok(list);
  }

  /**
   * 사용자 카테고리별 음료 전체조회 페이징처리
   * 매개변수 : CategoryId
   */
  @GetMapping("/categories/beverages/{categoryId}")
  public ResponseEntity<?> frontBeverages(
      @PathVariable Long categoryId,
      @SortDefault(sort = "id")
      Pageable pageable) {

    Page<BeverageDto> list = beverageService.frontBeverages(categoryId, pageable);

    return ResponseEntity.ok(list);
  }

  /**
   * 해당 음료 상세정보 조회
   * 매개변수 : BeverageId
   */
  @GetMapping("/beverage/{beverageId}")
  public ResponseEntity<?> beverageDetail(
      @PathVariable Long beverageId) {

    BeverageDto beverageDto = beverageService.beverageDetail(beverageId);

    return ResponseEntity.ok(beverageDto);
  }

}
