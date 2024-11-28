package com.zerobase.cafekiosk.beverage.entity;

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

}
