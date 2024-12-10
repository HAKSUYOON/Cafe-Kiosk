package com.zerobase.cafekiosk.member.dto;

import com.zerobase.cafekiosk.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDetailDto {

  private String username;
  private int stamp;
  private int saleCount;

  private static int calculateSaleCount(int stamp) {
    return stamp / 10;
  }

  public static MemberDetailDto of(Member member) {
    return MemberDetailDto.builder()
        .username(member.getUsername())
        .stamp(member.getStamp())
        .saleCount(calculateSaleCount(member.getStamp()))
        .build();
  }

}
