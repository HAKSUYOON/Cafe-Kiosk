package com.zerobase.cafekiosk.cart.controller;

import com.zerobase.cafekiosk.cart.dto.CartDto;
import com.zerobase.cafekiosk.cart.model.CartInput;
import com.zerobase.cafekiosk.cart.model.CartResult;
import com.zerobase.cafekiosk.cart.service.CartService;
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
@RequestMapping("/main/carts")
public class CartController {

  private final CartService cartService;

  /**
   * 현재 장바구니 조회
   * 요청 : kioskId
   */
  @GetMapping()
  public ResponseEntity<?> carts(@RequestBody CartInput request) {

    List<CartDto> list = cartService.list(request);
    int totalPrice = cartService.totalPrice(list);

    return ResponseEntity.ok().body(new CartResult<>(list, totalPrice));
  }

  /**
   * 장바구니 음료 추가
   * 요청 : CartInput
   */
  @PostMapping()
  public ResponseEntity<?> createCart(@RequestBody CartInput request) {

    CartDto cartDto = cartService.add(request);

    return ResponseEntity.ok(cartDto);
  }

  /**
   * 해당 장바구니 수량 증가버튼
   * 매개변수 : CartId
   */
  @PutMapping("/plus_button/{id}")
  public void upQuantity(@PathVariable Long id) {

    cartService.upQuantity(id);
  }

  /**
   * 해당 장바구니 수량 감소버튼
   * 매개변수 : CartId
   */
  @PutMapping("/minus_button/{id}")
  public void downQuantity(@PathVariable Long id) {

    cartService.downQuantity(id);
  }

  /**
   * 해당 장바구니 삭제버튼
   * 매개변수 : CartId
   */
  @DeleteMapping("/{id}")
  public void deleteCart(@PathVariable Long id) {

    cartService.delete(id);
  }
}
