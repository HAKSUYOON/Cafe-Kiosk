package com.zerobase.cafekiosk.order.entity;

import com.zerobase.cafekiosk.config.CartListConverter;
import com.zerobase.cafekiosk.order.constant.OrderStatus;
import java.util.List;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long kioskId;

  @Convert(converter = CartListConverter.class)
  private List<Long> cartIdList;

  private OrderStatus orderStatus;

  public OrderEntity buildOrderEntity(List<Long> cartIdList, Long kioskId) {
    return OrderEntity.builder()
        .kioskId(kioskId)
        .cartIdList(cartIdList)
        .orderStatus(OrderStatus.ORDER_STATUS_ORDERED)
        .build();
  }
}
