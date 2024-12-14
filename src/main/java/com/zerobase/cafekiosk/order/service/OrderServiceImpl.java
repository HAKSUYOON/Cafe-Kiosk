package com.zerobase.cafekiosk.order.service;

import com.zerobase.cafekiosk.beverage.repository.BeverageRepository;
import com.zerobase.cafekiosk.cart.constant.CartStatus;
import com.zerobase.cafekiosk.cart.entity.Cart;
import com.zerobase.cafekiosk.cart.repository.CartRepository;
import com.zerobase.cafekiosk.kiosk.repository.KioskRepository;
import com.zerobase.cafekiosk.order.constant.OrderStatus;
import com.zerobase.cafekiosk.order.dto.OrderDto;
import com.zerobase.cafekiosk.order.entity.OrderEntity;
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

  private List<Long> createCartIdList(Long kioskId) {
    List<Cart> cartList = cartRepository.findAllByKioskIdAndCartStatus(kioskId,
            CartStatus.CART_STATUS_ORDERED)
        .orElseThrow(() -> new RuntimeException("장바구니 아이템을 추가해주세요."));

    return cartList.stream().map(Cart::getId).collect(Collectors.toList());
  }

  private String convertToString(List<Long> cartIdList) {

    return cartIdList.stream().map(String::valueOf).collect(Collectors.joining(","));
  }

  private int getPrice(Long beverageId) {
    return beverageRepository.findById(beverageId)
        .orElseThrow(() -> new RuntimeException("존재하지 않는 음료입니다.")).getPrice();
  }

  private int totalQuantity(List<Long> cartIdList) {

    return cartRepository.findByIdIn(cartIdList)
        .orElseThrow(() -> new RuntimeException("해당 장바구니가 존재하지 않습니다.")).stream()
        .map(Cart::getQuantity).reduce(0, Integer::sum);
  }

  private int totalPrice(List<Long> cartIdList) {

    return cartRepository.findByIdIn(cartIdList)
        .orElseThrow(() -> new RuntimeException("해당 장바구니가 존재하지 않습니다.")).stream()
        .map(it -> it.getQuantity() * getPrice(it.getBeverageId())).reduce(0, Integer::sum);
  }

  @Override
  public OrderDto currentOrder(Long kioskId) {

    OrderEntity orderEntity = orderRepository.findByKioskIdAndOrderStatus(kioskId,
            OrderStatus.ORDER_STATUS_ORDERED)
        .orElseThrow(() -> new RuntimeException("주문이 존재하지 않습니다."));

    return OrderDto.of(orderEntity, totalQuantity(orderEntity.getCartIdList()),
        totalPrice(orderEntity.getCartIdList()));
  }

  @Override
  public OrderDto add(Long kioskId) {

    List<Long> cartIdList = createCartIdList(kioskId);

    if (orderRepository.existsByKioskIdAndCartIdListAndOrderStatus(kioskId,
        convertToString(cartIdList), OrderStatus.ORDER_STATUS_ORDERED)) {
      throw new RuntimeException("해당 주문은 이미 존재합니다.");
    }

    if (!kioskRepository.existsById(kioskId)) {
      throw new RuntimeException("해당 키오스크값이 존재하지 않습니다.");
    }

    OrderEntity orderEntity = new OrderEntity().buildOrderEntity(cartIdList, kioskId);

    orderRepository.save(orderEntity);

    return OrderDto.of(orderEntity, totalQuantity(orderEntity.getCartIdList()),
        totalPrice(orderEntity.getCartIdList()));
  }

  @Override
  @Transactional
  public void delete(Long kioskId) {

    if (!orderRepository.existsByKioskIdAndOrderStatus(kioskId, OrderStatus.ORDER_STATUS_ORDERED)) {
      throw new RuntimeException("주문이 존재하지 않습니다.");
    }

    orderRepository.deleteAllByKioskIdAndOrderStatus(kioskId, OrderStatus.ORDER_STATUS_ORDERED);

  }
}
