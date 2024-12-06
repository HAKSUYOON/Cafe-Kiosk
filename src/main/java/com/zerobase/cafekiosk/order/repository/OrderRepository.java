package com.zerobase.cafekiosk.order.repository;

import com.zerobase.cafekiosk.config.CartListConverter;
import com.zerobase.cafekiosk.order.constant.OrderStatus;
import com.zerobase.cafekiosk.order.entity.OrderEntity;
import java.util.Optional;
import javax.persistence.Convert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Convert(converter = CartListConverter.class)
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

  Optional<OrderEntity> findByKioskIdAndOrderStatus(Long kioskId, OrderStatus orderStatus);

  @Query(value = "SELECT * FROM Order_Entity o WHERE o.kiosk_id = ?1 AND o.cart_list = ?2 AND o.order_status = ?3", nativeQuery = true)
  Optional<OrderEntity> findByKioskIdAndCartListAndOrderStatus(Long id, String cartList,
      OrderStatus orderStatus);

  boolean existsByKioskIdAndOrderStatus(long kioskId, OrderStatus orderStatus);

  void deleteAllByKioskIdAndOrderStatus(long kioskId, OrderStatus orderStatus);
}
