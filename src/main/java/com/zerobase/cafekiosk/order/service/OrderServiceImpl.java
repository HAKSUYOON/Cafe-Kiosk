package com.zerobase.cafekiosk.order.service;

import com.zerobase.cafekiosk.beverage.repository.BeverageRepository;
import com.zerobase.cafekiosk.cart.constant.CartStatus;
import com.zerobase.cafekiosk.cart.entity.Cart;
import com.zerobase.cafekiosk.cart.repository.CartRepository;
import com.zerobase.cafekiosk.exception.Impl.AlreadyExistsOrderException;
import com.zerobase.cafekiosk.exception.Impl.NotFoundBeverageException;
import com.zerobase.cafekiosk.exception.Impl.NotFoundCardException;
import com.zerobase.cafekiosk.exception.Impl.NotFoundCartIdException;
import com.zerobase.cafekiosk.exception.Impl.NotFoundKioskException;
import com.zerobase.cafekiosk.exception.Impl.NotFoundOrderException;
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
        .orElseThrow(NotFoundCardException::new);

    return cartList.stream().map(Cart::getId).collect(Collectors.toList());
  }

  private String convertToString(List<Long> cartIdList) {

    return cartIdList.stream().map(String::valueOf).collect(Collectors.joining(","));
  }

  private int getPrice(Long beverageId) {
    return beverageRepository.findById(beverageId)
        .orElseThrow(NotFoundBeverageException::new).getPrice();
  }

  private int totalQuantity(List<Long> cartIdList) {

    return cartRepository.findByIdIn(cartIdList)
        .orElseThrow(NotFoundCartIdException::new).stream()
        .map(Cart::getQuantity).reduce(0, Integer::sum);
  }

  private int totalPrice(List<Long> cartIdList) {

    return cartRepository.findByIdIn(cartIdList)
        .orElseThrow(NotFoundCartIdException::new).stream()
        .map(it -> it.getQuantity() * getPrice(it.getBeverageId())).reduce(0, Integer::sum);
  }

  @Override
  public OrderDto currentOrder(Long kioskId) {

    OrderEntity orderEntity = orderRepository.findByKioskIdAndOrderStatus(kioskId,
            OrderStatus.ORDER_STATUS_ORDERED)
        .orElseThrow(NotFoundOrderException::new);

    return OrderDto.of(orderEntity, totalQuantity(orderEntity.getCartIdList()),
        totalPrice(orderEntity.getCartIdList()));
  }

  @Override
  public OrderDto add(Long kioskId) {

    List<Long> cartIdList = createCartIdList(kioskId);

    if (orderRepository.existsByKioskIdAndCartIdListAndOrderStatus(kioskId,
        convertToString(cartIdList), OrderStatus.ORDER_STATUS_ORDERED)) {
      throw new AlreadyExistsOrderException();
    }

    if (!kioskRepository.existsById(kioskId)) {
      throw new NotFoundKioskException();
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
      throw new NotFoundOrderException();
    }

    orderRepository.deleteAllByKioskIdAndOrderStatus(kioskId, OrderStatus.ORDER_STATUS_ORDERED);

  }
}
