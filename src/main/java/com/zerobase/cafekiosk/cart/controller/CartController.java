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

  @GetMapping()
  public ResponseEntity<?> carts(@RequestBody CartInput request) {

    List<CartDto> list = cartService.list(request);
    int totalPrice = cartService.totalPrice(list);

    return ResponseEntity.ok().body(new CartResult<>(list, totalPrice));
  }

  @PostMapping()
  public ResponseEntity<?> createCart(@RequestBody CartInput request) {

    CartDto cartDto = cartService.add(request);

    return ResponseEntity.ok(cartDto);
  }

  @PutMapping("/plus_button/{id}")
  public void upQuantity(@PathVariable Long id) {

    cartService.upQuantity(id);
  }

  @PutMapping("/minus_button/{id}")
  public void downQuantity(@PathVariable Long id) {

    cartService.downQuantity(id);
  }

  @DeleteMapping("/{id}")
  public void deleteCart(@PathVariable Long id) {

    cartService.delete(id);
  }
}
