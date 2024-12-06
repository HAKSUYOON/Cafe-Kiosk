package com.zerobase.cafekiosk.order.service;

import com.zerobase.cafekiosk.beverage.repository.BeverageRepository;
import com.zerobase.cafekiosk.cart.constant.CartStatus;
import com.zerobase.cafekiosk.cart.entity.Cart;
import com.zerobase.cafekiosk.cart.repository.CartRepository;
import com.zerobase.cafekiosk.kiosk.repository.KioskRepository;
import com.zerobase.cafekiosk.order.constant.OrderStatus;
import com.zerobase.cafekiosk.order.dto.OrderDto;
import com.zerobase.cafekiosk.order.entity.OrderEntity;
import com.zerobase.cafekiosk.order.model.OrderInput;
import com.zerobase.cafekiosk.order.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final CartRepository cartRepository;
  private final BeverageRepository beverageRepository;
  private final KioskRepository kioskRepository;

  private List<Long> createCartList() {
    List<Cart> cartList = cartRepository.findAllByKioskIdAndCartStatus(1L, CartStatus.ORDERED)
        .orElseThrow(() -> new RuntimeException("장바구니 아이템을 추가해주세요."));

    return cartList.stream().map(Cart::getId).collect(Collectors.toList());
  }

  private String convertToString(List<Long> cartList) {

    return cartList.stream().map(String::valueOf).collect(Collectors.joining(","));
  }

  private int getPrice(Long beverageId) {
    return beverageRepository.findById(beverageId)
        .orElseThrow(() -> new RuntimeException("존재하지 않는 음료입니다.")).getPrice();
  }

  private int totalQuantity(List<Long> cartList) {

    return cartList.stream().map(it -> cartRepository.findById(it)
            .orElseThrow(() -> new RuntimeException("해당 장바구니가 존재하지 않습니다.")))
        .map(Cart::getQuantity).reduce(0, Integer::sum);
  }

  private int totalPrice(List<Long> cartList) {

    return cartList.stream().map(it -> cartRepository.findById(it)
            .orElseThrow(() -> new RuntimeException("해당 장바구니가 존재하지 않습니다.")))
        .map(it -> it.getQuantity() * getPrice(it.getBeverageId())).reduce(0, Integer::sum);
  }

  @Override
  public OrderDto list() {

    OrderEntity orderEntity = orderRepository.findByKioskIdAndOrderStatus(1L, OrderStatus.ORDERED)
        .orElseThrow(() -> new RuntimeException("주문이 존재하지 않습니다."));

    return OrderDto.of(orderEntity, totalQuantity(orderEntity.getCartList()),
        totalPrice(orderEntity.getCartList()));
  }

  @Override
  public OrderDto add(OrderInput request) {

    List<Long> cartList = createCartList();

    OrderEntity orderEntity = orderRepository.findByKioskIdAndCartListAndOrderStatus(
            request.getKioskId(), convertToString(cartList), OrderStatus.ORDERED)
        .orElseGet(() -> {
          if (!kioskRepository.existsById(request.getKioskId())) {
            throw new RuntimeException("존재하지 않는 키오스크값입니다.");
          }
          return new OrderEntity().buildOrderEntity(cartList, request);
        });

    orderRepository.save(orderEntity);

    return OrderDto.of(orderEntity, totalQuantity(orderEntity.getCartList()),
        totalPrice(orderEntity.getCartList()));
  }

  @Override
  @Transactional
  public void delete() {

    if (!orderRepository.existsByKioskIdAndOrderStatus(1L, OrderStatus.ORDERED)) {
      throw new RuntimeException("주문이 존재하지 않습니다.");
    }

    orderRepository.deleteAllByKioskIdAndOrderStatus(1L, OrderStatus.ORDERED);

  }
}
