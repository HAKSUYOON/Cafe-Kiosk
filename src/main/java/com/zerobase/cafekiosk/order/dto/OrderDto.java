package com.zerobase.cafekiosk.order.dto;

import com.zerobase.cafekiosk.order.entity.OrderEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

  private Long id;
  private List<Long> cartIdList;
  private int totalQuantity;
  private int totalPrice;

  public static OrderDto of(OrderEntity orderEntity, int totalQuantity, int totalPrice) {

    return OrderDto.builder()
        .id(orderEntity.getId())
        .cartIdList(orderEntity.getCartIdList())
        .totalQuantity(totalQuantity)
        .totalPrice(totalPrice)
        .build();
  }
}
