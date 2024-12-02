package com.zerobase.cafekiosk.cart.model;

import javax.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartInput {

  private Long id;
  private Long beverageId;
  private String beverageName;
  private int price;
  @Positive
  private int quantity;

}
