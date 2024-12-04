package com.zerobase.cafekiosk.cart.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CartResult<T> {
  private T data;
  private int totalPrice;
}
