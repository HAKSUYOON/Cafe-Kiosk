package com.zerobase.cafekiosk.payment.entity;

import com.zerobase.cafekiosk.member.entity.Member;
import com.zerobase.cafekiosk.order.entity.OrderEntity;
import com.zerobase.cafekiosk.payment.constant.PaymentMethod;
import com.zerobase.cafekiosk.payment.constant.PaymentStatus;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long kioskId;

  private Long orderId;

  private String username;

  private boolean isSale;

  @PositiveOrZero
  private int usingStampCount;

  @PositiveOrZero
  private int totalAmount;

  @PositiveOrZero
  private int saleAmount;

  @PositiveOrZero
  private int approvedAmount;

  private PaymentMethod paymentMethod;

  private LocalDateTime requestedAt;

  private LocalDateTime approvedAt;

  private PaymentStatus paymentStatus;

  public static Payment buildPayment(Long kioskId, OrderEntity orderEntity, Member member,
      boolean isSale, int usingStampCount, int totalAmount, int saleAmount, int approvedAmount) {
    return Payment.builder()
        .kioskId(kioskId)
        .orderId(orderEntity.getId())
        .username(member.getUsername())
        .isSale(isSale)
        .usingStampCount(usingStampCount)
        .totalAmount(totalAmount)
        .saleAmount(saleAmount)
        .approvedAmount(approvedAmount)
        .requestedAt(LocalDateTime.now())
        .paymentStatus(PaymentStatus.PAYMENT_STATUS_READY)
        .build();
  }

  public static Payment freePayment(Payment payment) {
    payment.setUsername("ADMIN");
    payment.setSale(false);
    payment.setUsingStampCount(0);
    payment.setTotalAmount(0);
    payment.setSaleAmount(0);
    payment.setApprovedAmount(0);
    payment.setPaymentMethod(PaymentMethod.PAYMENT_METHOD_CARD);
    payment.setApprovedAt(LocalDateTime.now());
    payment.setPaymentStatus(PaymentStatus.PAYMENT_STATUS_DONE);
    return payment;
  }
}
