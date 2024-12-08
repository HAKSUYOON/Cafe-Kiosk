package com.zerobase.cafekiosk.cart.service;

import com.zerobase.cafekiosk.cart.dto.CartDto;
import com.zerobase.cafekiosk.cart.model.CartInput;
import java.util.List;

public interface CartService {

  List<CartDto> list(CartInput request);

  CartDto add(CartInput request);

  void upQuantity(Long id);

  void downQuantity(Long id);

  void delete(Long id);

  int totalPrice(List<CartDto> list);
}
