package com.zerobase.cafekiosk.beverage.entity;

import com.zerobase.cafekiosk.beverage.model.BeverageInput;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Entity
@Data
@Builder
@AllArgsConstructor
public class Beverage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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

  public Beverage buildBeverage(BeverageInput request) {
    return Beverage.builder()
        .categoryId(request.getCategoryId())
        .beverageName(request.getBeverageName())
        .price(request.getPrice())
        .beverageDetail(request.getBeverageDetail())
        .shot(request.getShot())
        .hotWater(request.getHotWater())
        .coldWater(request.getColdWater())
        .ice(request.isIce())
        .powder1(request.getPowder1())
        .powder2(request.getPowder2())
        .powder3(request.getPowder3())
        .build();
  }

  public void setBeverage(Beverage beverage, BeverageInput request) {
    beverage.setCategoryId(request.getCategoryId());
    beverage.setBeverageName(request.getBeverageName());
    beverage.setPrice(request.getPrice());
    beverage.setBeverageDetail(request.getBeverageDetail());
    beverage.setShot(request.getShot());
    beverage.setHotWater(request.getHotWater());
    beverage.setColdWater(request.getColdWater());
    beverage.setIce(request.isIce());
    beverage.setPowder1(request.getPowder1());
    beverage.setPowder2(request.getPowder2());
    beverage.setPowder3(request.getPowder3());
  }
}
