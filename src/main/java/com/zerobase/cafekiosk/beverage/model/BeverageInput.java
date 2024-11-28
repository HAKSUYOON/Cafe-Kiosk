package com.zerobase.cafekiosk.beverage.model;

import lombok.Data;

@Data
public class BeverageInput {

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

}
