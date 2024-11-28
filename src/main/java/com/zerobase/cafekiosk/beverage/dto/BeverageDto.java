package com.zerobase.cafekiosk.beverage.dto;

import com.zerobase.cafekiosk.beverage.entity.Beverage;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeverageDto {

  private Long id;
  private long categoryId;
  private String beverageName;
  private int price;
  private String beverageDetail;
  private int shot;
  private int hotWater;
  private int coldWater;
  private boolean ice;
  private int powder1;
  private int powder2;
  private int powder3;

  public static BeverageDto of(Beverage beverage) {
    return BeverageDto.builder()
        .id(beverage.getId())
        .categoryId(beverage.getCategoryId())
        .beverageName(beverage.getBeverageName())
        .price(beverage.getPrice())
        .beverageDetail(beverage.getBeverageDetail())
        .shot(beverage.getShot())
        .hotWater(beverage.getHotWater())
        .coldWater(beverage.getColdWater())
        .ice(beverage.isIce())
        .powder1(beverage.getPowder1())
        .powder2(beverage.getPowder2())
        .powder3(beverage.getPowder3())
        .build();
  }

  public static List<BeverageDto> of(List<Beverage> beverages) {

    return beverages.stream().map(BeverageDto::of).collect(Collectors.toList());
  }

}


