package com.zerobase.cafekiosk.order.service;

import com.zerobase.cafekiosk.order.dto.OrderDto;
import com.zerobase.cafekiosk.order.model.OrderInput;

public interface OrderService {

  OrderDto currentOrder(Long kioskId);

  OrderDto add(Long kioskId);

  void delete(Long kioskId);
}
