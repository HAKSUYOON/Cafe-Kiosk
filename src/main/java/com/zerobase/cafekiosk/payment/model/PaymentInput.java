package com.zerobase.cafekiosk.payment.model;

import lombok.Data;

@Data
public class PaymentInput {

  private Long kioskId;
  private String cardNumber;

}
