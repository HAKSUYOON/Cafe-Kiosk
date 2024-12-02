package com.zerobase.cafekiosk.cart.service;

import com.zerobase.cafekiosk.beverage.entity.Beverage;
import com.zerobase.cafekiosk.beverage.repository.BeverageRepository;
import com.zerobase.cafekiosk.cart.constant.CartStatus;
import com.zerobase.cafekiosk.cart.dto.CartDto;
import com.zerobase.cafekiosk.cart.entity.Cart;
import com.zerobase.cafekiosk.cart.model.CartInput;
import com.zerobase.cafekiosk.cart.repository.CartRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

  private final CartRepository cartRepository;
  private final BeverageRepository beverageRepository;

  public static int calculatePrice(CartDto cartDto) {
    return cartDto.getQuantity() * cartDto.getPrice();
  }

  @Override
  public List<CartDto> list() {

    List<Cart> carts = cartRepository.findAll();

    return CartDto.of(carts);
  }

  @Override
  public CartDto add(CartInput request) {

    Optional<Cart> optionalCart = cartRepository.findByBeverageIdAndCartStatus
        (request.getBeverageId(), CartStatus.ORDERED);

    if (optionalCart.isPresent()) {
      Cart cart = optionalCart.get();
      cart.setQuantity(cart.getQuantity() + request.getQuantity());
      cartRepository.save(cart);
      return CartDto.of(cart);
    } else {
      Beverage beverage = beverageRepository.findById(request.getBeverageId())
          .orElseThrow(() -> new RuntimeException("존재하지 않는 음료입니다."));
      request.setBeverageName(beverage.getBeverageName());
      request.setPrice(beverage.getPrice());
      Cart cart = new Cart().buildCart(request);
      cartRepository.save(cart);
      return CartDto.of(cart);
    }
  }

  @Override
  public void upQuantity(Long id) {

    Cart cart = cartRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("존재하지 않는 장바구니 아이템입니다."));

    cart.setQuantity(cart.getQuantity() + 1);
    cartRepository.save(cart);
  }

  @Override
  public void downQuantity(Long id) {

    Cart cart = cartRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("존재하지 않는 장바구니 아이템입니다."));

    if (cart.getQuantity() == 1) {
      cartRepository.deleteById(id);
    } else {
      cart.setQuantity(cart.getQuantity() - 1);
      cartRepository.save(cart);
    }
  }

  @Override
  public void delete(Long id) {
    if (!cartRepository.existsById(id)) {
      throw new RuntimeException("존재하지 않는 장바구니 아이템입니다.");
    }
    cartRepository.deleteById(id);
  }

  @Override
  public int totalPrice(List<CartDto> list) {
    int totalPrice = 0;
    for (CartDto x : list) {
      totalPrice += calculatePrice(x);
    }
    return totalPrice;
  }
}
