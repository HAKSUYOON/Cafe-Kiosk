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

  @Query(value = "SELECT IF(count(*)>0, 'true', 'false') FROM Order_Entity o WHERE o.kiosk_id = ?1 AND o.cart_id_list = ?2 AND o.order_status = ?3 limit 1" , nativeQuery = true)
  boolean existsByKioskIdAndCartIdListAndOrderStatus(Long id, String cartIdList,
      OrderStatus orderStatus);

  boolean existsByKioskIdAndOrderStatus(long kioskId, OrderStatus orderStatus);

  void deleteAllByKioskIdAndOrderStatus(long kioskId, OrderStatus orderStatus);
}
