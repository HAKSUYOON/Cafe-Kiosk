package com.zerobase.cafekiosk.beverage.controller;

import com.zerobase.cafekiosk.beverage.dto.BeverageDto;
import com.zerobase.cafekiosk.beverage.model.BeverageInput;
import com.zerobase.cafekiosk.beverage.service.BeverageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
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
@RequestMapping("/admin/beverages")
public class BeverageController {

  private final BeverageService beverageService;

  /**
   * 음료 전체 조회 페이징 처리
   */
  @GetMapping
  public ResponseEntity<?> findAllBeverages(
      @SortDefault.SortDefaults({@SortDefault(sort = "id"), @SortDefault(sort = "categoryId")})
      Pageable pageable) {
    Page<BeverageDto> list = beverageService.list(pageable);

    return ResponseEntity.ok(list);
  }

  /**
   * 음료 생성
   * 요청 : BeverageInput
   */
  @PostMapping
  public ResponseEntity<?> createBeverage(@RequestBody BeverageInput request) {
    BeverageDto beverageDto = beverageService.add(request);
    return ResponseEntity.ok(beverageDto);
  }

  /**
   * 음료 수정
   * 매개변수 : 음료 Id
   * 요청 : BeverageInput
   */
  @PutMapping("/{id}")
  public void updateBeverage(@PathVariable Long id, @RequestBody BeverageInput request) {
    beverageService.update(id, request);
  }

  /**
   * 음료 삭제
   * 매개변수 : 음료 Id
   */
  @DeleteMapping("/{id}")
  public void deleteBeverage(@PathVariable Long id) {
    beverageService.delete(id);
  }

}
