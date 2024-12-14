package com.zerobase.cafekiosk.cart.service;

import com.zerobase.cafekiosk.beverage.repository.BeverageRepository;
import com.zerobase.cafekiosk.cart.constant.CartStatus;
import com.zerobase.cafekiosk.cart.dto.CartDto;
import com.zerobase.cafekiosk.cart.entity.Cart;
import com.zerobase.cafekiosk.cart.model.CartInput;
import com.zerobase.cafekiosk.cart.repository.CartRepository;
import com.zerobase.cafekiosk.kiosk.repository.KioskRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

  private final CartRepository cartRepository;
  private final BeverageRepository beverageRepository;
  private final KioskRepository kioskRepository;

  private int calculatePrice(CartDto cartDto) {
    return cartDto.getQuantity() * cartDto.getPrice();
  }

  private String getBeverageName(Long beverageId) {
    return beverageRepository.findById(beverageId)
        .orElseThrow(() -> new RuntimeException("존재하지 않는 음료입니다.")).getBeverageName();
  }

  private int getPrice(Long beverageId) {
    return beverageRepository.findById(beverageId)
        .orElseThrow(() -> new RuntimeException("존재하지 않는 음료입니다.")).getPrice();
  }

  @Override
  public List<CartDto> list(CartInput request) {

    List<Cart> carts = cartRepository.findAllByKioskIdAndCartStatus(request.getKioskId(), CartStatus.CART_STATUS_ORDERED)
        .orElseThrow(() -> new RuntimeException("상품을 추가해주세요."));

    return carts.stream().map(
            it -> CartDto.of(it, getBeverageName(it.getBeverageId()), getPrice(it.getBeverageId())))
        .collect(Collectors.toList());
  }

  @Override
  public CartDto add(CartInput request) {

    Cart cart = cartRepository.findByKioskIdAndBeverageIdAndCartStatus
        (request.getKioskId(), request.getBeverageId(), CartStatus.CART_STATUS_ORDERED).map(it -> {
      it.setQuantity(it.getQuantity() + request.getQuantity());
      return it;
    }).orElseGet(() -> {
      if (!beverageRepository.existsById(request.getBeverageId())) {
        throw new RuntimeException("존재하지 않는 음료입니다.");
      }
      if (!kioskRepository.existsById(request.getKioskId())) {
        throw new RuntimeException("존재하지 않는 키오스크번호입니다.");
      }
      return new Cart().buildCart(request);
    });

    cartRepository.save(cart);

    return CartDto.of(cart, getBeverageName(cart.getBeverageId()), getPrice(cart.getBeverageId()));
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

    return list.stream().map(this::calculatePrice).reduce(0, Integer::sum);
  }
}
