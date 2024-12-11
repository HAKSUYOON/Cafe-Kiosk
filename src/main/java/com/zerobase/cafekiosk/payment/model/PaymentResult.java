package com.zerobase.cafekiosk.payment.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PaymentResult<T> {
  private T paymentDto;
  private T cardDto;
}
