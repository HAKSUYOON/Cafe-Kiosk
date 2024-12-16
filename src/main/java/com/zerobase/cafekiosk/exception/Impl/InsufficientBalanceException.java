package com.zerobase.cafekiosk.exception.Impl;

import com.zerobase.cafekiosk.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class InsufficientBalanceException extends AbstractException {

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return "잔액이 부족합니다.";
  }
}