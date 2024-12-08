package com.zerobase.cafekiosk.order.service;

import com.zerobase.cafekiosk.order.dto.OrderDto;
import com.zerobase.cafekiosk.order.model.OrderInput;

public interface OrderService {

  OrderDto currentOrder(OrderInput request);

  OrderDto add(OrderInput request);

  void delete(OrderInput request);
}
