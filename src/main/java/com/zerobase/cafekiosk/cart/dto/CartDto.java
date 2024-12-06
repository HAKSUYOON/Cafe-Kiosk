package com.zerobase.cafekiosk.cart.dto;

import com.zerobase.cafekiosk.cart.constant.CartStatus;
import com.zerobase.cafekiosk.cart.entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

  private Long id;
  private Long beverageId;
  private Long kioskId;
  private String beverageName;
  private int price;
  private int quantity;
  private CartStatus cartStatus;

  public static CartDto of (Cart cart, String beverageName, int price) {
    return CartDto.builder()
        .id(cart.getId())
        .beverageId(cart.getBeverageId())
        .kioskId(cart.getKioskId())
        .beverageName(beverageName)
        .price(price)
        .quantity(cart.getQuantity())
        .cartStatus(cart.getCartStatus())
        .build();
  }
}
