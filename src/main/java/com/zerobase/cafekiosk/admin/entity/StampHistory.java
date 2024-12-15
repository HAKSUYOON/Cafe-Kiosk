package com.zerobase.cafekiosk.admin.entity;

import com.zerobase.cafekiosk.payment.entity.Payment;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StampHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long kioskId;
  private Long paymentId;
  private LocalDateTime approvedAt;

  private String username;
  private int beforeStamp;
  private int afterStamp;

  public static StampHistory buildStampHistory(Payment payment, int afterStamp, int beforeStamp) {

    return StampHistory.builder()
        .kioskId(payment.getKioskId())
        .paymentId(payment.getId())
        .approvedAt(payment.getApprovedAt())
        .username(payment.getUsername())
        .beforeStamp(beforeStamp)
        .afterStamp(afterStamp)
        .build();
  }
}
