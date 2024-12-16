package com.zerobase.cafekiosk.exception.Impl;

import com.zerobase.cafekiosk.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotFoundCardException extends AbstractException {

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return "인증되지 않은 카드입니다. 사용할 수 있는 카드는 아래와 같습니다.\n"
        + "1111_1111_1111_1111 : 체크카드, 사용가능\n"
        + "2222_2222_2222_2222 : 신용카드, 사용가능\n"
        + "3333_3333_3333_3333 : 체크카드, 거래정지\n"
        + "4444_4444_4444_4444 : 신용카드, 거래정지";
  }
}