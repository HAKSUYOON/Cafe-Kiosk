package com.zerobase.cafekiosk.payment.dto;

import com.zerobase.cafekiosk.payment.constant.PaymentMethod;
import com.zerobase.cafekiosk.payment.constant.PaymentStatus;
import com.zerobase.cafekiosk.payment.entity.Payment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

  private Long id;
  private Long kioskId;
  private Long orderId;
  private String username;
  private boolean isSale;
  private int usingStampCount;
  private int totalAmount;
  private int saleAmount;
  private int approvedAmount;
  private PaymentMethod paymentMethod;
  private LocalDateTime requestedAt;
  private LocalDateTime approvedAt;
  private PaymentStatus paymentStatus;

  public static PaymentDto of (Payment payment) {
    return PaymentDto.builder()
        .id(payment.getId())
        .kioskId(payment.getKioskId())
        .orderId(payment.getOrderId())
        .username(payment.getUsername())
        .isSale(payment.isSale())
        .usingStampCount(payment.getUsingStampCount())
        .totalAmount(payment.getTotalAmount())
        .saleAmount(payment.getSaleAmount())
        .approvedAmount(payment.getApprovedAmount())
        .paymentMethod(payment.getPaymentMethod())
        .requestedAt(payment.getRequestedAt())
        .approvedAt(payment.getApprovedAt())
        .paymentStatus(payment.getPaymentStatus())
        .build();
  }

}
