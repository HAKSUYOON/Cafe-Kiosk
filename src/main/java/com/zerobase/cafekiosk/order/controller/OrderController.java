package com.zerobase.cafekiosk.order.controller;

import com.zerobase.cafekiosk.order.dto.OrderDto;
import com.zerobase.cafekiosk.order.model.OrderInput;
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

  @GetMapping
  public ResponseEntity<?> currentOrder(@RequestBody OrderInput request) {

    OrderDto orderDto = orderService.currentOrder(request);

    return ResponseEntity.ok(orderDto);
  }

  @PostMapping
  public ResponseEntity<?> add(@RequestBody OrderInput request) {

    OrderDto orderDto = orderService.add(request);

    return ResponseEntity.ok(orderDto);
  }

  @DeleteMapping("/backspace_button")
  public void delete(@RequestBody OrderInput request) {

    orderService.delete(request);
  }
}
