package com.zerobase.cafekiosk.exception.Impl;

import com.zerobase.cafekiosk.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotFoundOrderException extends AbstractException {

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return "존재하지 않는 주문내역입니다.";
  }
}