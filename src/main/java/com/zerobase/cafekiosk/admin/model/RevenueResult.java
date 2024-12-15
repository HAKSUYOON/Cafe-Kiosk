package com.zerobase.cafekiosk.admin.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RevenueResult<T> {
  private T data;
  private int revenues;
  private int count;

}
