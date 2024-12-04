package com.zerobase.cafekiosk.cart.repository;

import com.zerobase.cafekiosk.cart.constant.CartStatus;
import com.zerobase.cafekiosk.cart.entity.Cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

  Optional<Cart> findByBeverageIdAndCartStatus(Long beverageId, CartStatus cartStatus);
}
