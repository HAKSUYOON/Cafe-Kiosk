package com.zerobase.cafekiosk.order.controller;

import com.zerobase.cafekiosk.order.dto.OrderDto;
import com.zerobase.cafekiosk.order.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/main/order")
public class OrderController {

  private final OrderService orderService;

  /**
   * 현재 주문내역 확인
   * 요청 : KioskId
   */
  @GetMapping
  public ResponseEntity<?> currentOrder(@RequestBody Long kioskId) {

    OrderDto orderDto = orderService.currentOrder(kioskId);

    return ResponseEntity.ok(orderDto);
  }

  /**
   * 현재 장바구니로부터 주문 생성
   * 요청 : KioskId
   */
  @PostMapping
  public ResponseEntity<?> add(@RequestBody Long kioskId) {

    OrderDto orderDto = orderService.add(kioskId);

    return ResponseEntity.ok(orderDto);
  }

  /**
   * 주문 취소, 장바구니로 돌아가기
   * 요청 : KioskId
   */
  @DeleteMapping("/backspace_button")
  public void delete(@RequestBody Long kioskId) {

    orderService.delete(kioskId);
  }
}
