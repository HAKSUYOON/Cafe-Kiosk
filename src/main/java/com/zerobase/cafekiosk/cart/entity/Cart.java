package com.zerobase.cafekiosk.cart.entity;

import com.zerobase.cafekiosk.cart.constant.CartStatus;
import com.zerobase.cafekiosk.cart.model.CartInput;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long beverageId;

  @Positive
  private int quantity;

  private CartStatus cartStatus;

  public Cart buildCart(CartInput request) {
    return Cart.builder()
        .beverageId(request.getBeverageId())
        .quantity(request.getQuantity())
        .cartStatus(CartStatus.ORDERED)
        .build();
  }
}
