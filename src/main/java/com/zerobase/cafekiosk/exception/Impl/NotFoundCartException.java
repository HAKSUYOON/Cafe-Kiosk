package com.zerobase.cafekiosk.exception.Impl;

import com.zerobase.cafekiosk.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotFoundCartException extends AbstractException {

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return "장바구니가 비어있습니다. 상품을 추가해주세요.";
  }
}