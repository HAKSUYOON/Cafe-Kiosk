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

  private List<Long> createCartIdList(OrderInput request) {
    List<Cart> cartList = cartRepository.findAllByKioskIdAndCartStatus(request.getKioskId(),
            CartStatus.ORDERED)
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
  public OrderDto currentOrder(OrderInput request) {

    OrderEntity orderEntity = orderRepository.findByKioskIdAndOrderStatus(request.getKioskId(),
            OrderStatus.ORDERED)
        .orElseThrow(() -> new RuntimeException("주문이 존재하지 않습니다."));

    return OrderDto.of(orderEntity, totalQuantity(orderEntity.getCartIdList()),
        totalPrice(orderEntity.getCartIdList()));
  }

  @Override
  public OrderDto add(OrderInput request) {

    List<Long> cartIdList = createCartIdList(request);

    if (orderRepository.existsByKioskIdAndCartIdListAndOrderStatus(request.getKioskId(),
        convertToString(cartIdList), OrderStatus.ORDERED)) {
      throw new RuntimeException("해당 주문은 이미 존재합니다.");
    }

    if (!kioskRepository.existsById(request.getKioskId())) {
      throw new RuntimeException("해당 키오스크값이 존재하지 않습니다.");
    }

    OrderEntity orderEntity = new OrderEntity().buildOrderEntity(cartIdList, request);

    orderRepository.save(orderEntity);

    return OrderDto.of(orderEntity, totalQuantity(orderEntity.getCartIdList()),
        totalPrice(orderEntity.getCartIdList()));
  }

  @Override
  @Transactional
  public void delete(OrderInput request) {

    if (!orderRepository.existsByKioskIdAndOrderStatus(request.getKioskId(), OrderStatus.ORDERED)) {
      throw new RuntimeException("주문이 존재하지 않습니다.");
    }

    orderRepository.deleteAllByKioskIdAndOrderStatus(request.getKioskId(), OrderStatus.ORDERED);

  }
}
