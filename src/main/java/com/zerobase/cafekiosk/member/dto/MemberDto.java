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
public class MemberDto {

  private String username;
  private int stamp;

  public static MemberDto of(Member member) {
    return MemberDto.builder()
        .username(member.getUsername())
        .stamp(member.getStamp())
        .build();
  }

}
