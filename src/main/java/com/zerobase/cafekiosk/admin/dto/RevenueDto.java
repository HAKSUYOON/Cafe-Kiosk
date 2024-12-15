package com.zerobase.cafekiosk.admin.dto;

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
public class RevenueDto {

  private Long id;
  private Long kioskId;
  private int approvedAmount;
  private LocalDateTime approvedAt;

  public static RevenueDto of(Payment payment) {
    return RevenueDto.builder()
        .id(payment.getId())
        .kioskId(payment.getKioskId())
        .approvedAmount(payment.getApprovedAmount())
        .approvedAt(payment.getApprovedAt())
        .build();
  }
}
