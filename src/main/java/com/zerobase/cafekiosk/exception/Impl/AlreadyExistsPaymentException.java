package com.zerobase.cafekiosk.exception.Impl;

import com.zerobase.cafekiosk.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadyExistsPaymentException extends AbstractException {

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return "이미 존재하는 결제내역입니다.";
  }
}
