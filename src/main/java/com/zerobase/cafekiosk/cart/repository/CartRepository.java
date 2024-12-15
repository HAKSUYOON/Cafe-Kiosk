package com.zerobase.cafekiosk.cart.repository;

import com.zerobase.cafekiosk.cart.constant.CartStatus;
import com.zerobase.cafekiosk.cart.entity.Cart;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

  Optional<List<Cart>> findAllByKioskIdAndCartStatus(Long kioskId, CartStatus cartStatus);

  Optional<Cart> findByKioskIdAndBeverageIdAndCartStatus(long kioskId, Long beverageId, CartStatus cartStatus);

  Optional<List<Cart>> findByIdIn(List<Long> cartIdList);

  Optional<Cart> findByIdAndKioskIdAndCartStatus(Long cartId, Long kioskId, CartStatus cartStatus);

  Optional<Cart> findByIdAndCartStatus(Long cartId, CartStatus cartStatus);
}
