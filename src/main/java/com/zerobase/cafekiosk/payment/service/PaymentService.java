package com.zerobase.cafekiosk.payment.service;

import com.zerobase.cafekiosk.payment.dto.PaymentDto;
import com.zerobase.cafekiosk.payment.model.PaymentInput;

public interface PaymentService {

  PaymentDto createPayment(Long kioskId, Object principal);

  PaymentDto confirm(Long paymentId, Long kioskId);

  void setStamp(Long paymentId, PaymentInput request);
}
