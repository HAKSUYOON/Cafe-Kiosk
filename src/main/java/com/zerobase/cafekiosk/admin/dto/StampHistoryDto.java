package com.zerobase.cafekiosk.admin.dto;

import com.zerobase.cafekiosk.admin.entity.StampHistory;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StampHistoryDto {

  private Long id;

  private Long kioskId;
  private Long paymentId;
  private LocalDateTime approvedAt;

  private String username;
  private int beforeStamp;
  private int afterStamp;

  public static StampHistoryDto of (StampHistory stampHistory) {
    return StampHistoryDto.builder()
        .id(stampHistory.getId())
        .kioskId(stampHistory.getKioskId())
        .paymentId(stampHistory.getPaymentId())
        .approvedAt(stampHistory.getApprovedAt())
        .username(stampHistory.getUsername())
        .beforeStamp(stampHistory.getBeforeStamp())
        .afterStamp(stampHistory.getAfterStamp())
        .build();
  }

}
