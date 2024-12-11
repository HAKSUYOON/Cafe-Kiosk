package com.zerobase.cafekiosk.payment.service;

import com.zerobase.cafekiosk.payment.dto.PaymentDto;

public interface PaymentService {

  PaymentDto createPayment(Long kioskId, Object principal);

  PaymentDto confirm(Long paymentId, Long kioskId);
}
