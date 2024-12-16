package com.zerobase.cafekiosk.cart.service;

import com.zerobase.cafekiosk.beverage.repository.BeverageRepository;
import com.zerobase.cafekiosk.cart.constant.CartStatus;
import com.zerobase.cafekiosk.cart.dto.CartDto;
import com.zerobase.cafekiosk.cart.entity.Cart;
import com.zerobase.cafekiosk.cart.model.CartInput;
import com.zerobase.cafekiosk.cart.repository.CartRepository;
import com.zerobase.cafekiosk.exception.Impl.NotFoundBeverageException;
import com.zerobase.cafekiosk.exception.Impl.NotFoundCartException;
import com.zerobase.cafekiosk.exception.Impl.NotFoundCartIdException;
import com.zerobase.cafekiosk.exception.Impl.NotFoundKioskException;
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
        .orElseThrow(NotFoundBeverageException::new).getBeverageName();
  }

  private int getPrice(Long beverageId) {
    return beverageRepository.findById(beverageId)
        .orElseThrow(NotFoundBeverageException::new).getPrice();
  }

  @Override
  public List<CartDto> list(CartInput request) {

    List<Cart> carts = cartRepository.findAllByKioskIdAndCartStatus(request.getKioskId(), CartStatus.CART_STATUS_ORDERED)
        .orElseThrow(NotFoundCartException::new);

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
        throw new NotFoundBeverageException();
      }
      if (!kioskRepository.existsById(request.getKioskId())) {
        throw new NotFoundKioskException();
      }
      return new Cart().buildCart(request);
    });

    cartRepository.save(cart);

    return CartDto.of(cart, getBeverageName(cart.getBeverageId()), getPrice(cart.getBeverageId()));
  }

  @Override
  public void upQuantity(Long id) {

    Cart cart = cartRepository.findById(id)
        .orElseThrow(NotFoundCartIdException::new);

    cart.setQuantity(cart.getQuantity() + 1);
    cartRepository.save(cart);
  }

  @Override
  public void downQuantity(Long id) {

    Cart cart = cartRepository.findById(id)
        .orElseThrow(NotFoundCartIdException::new);

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
      throw new NotFoundCartIdException();
    }
    cartRepository.deleteById(id);
  }

  @Override
  public int totalPrice(List<CartDto> list) {

    return list.stream().map(this::calculatePrice).reduce(0, Integer::sum);
  }
}
