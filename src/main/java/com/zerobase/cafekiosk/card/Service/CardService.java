package com.zerobase.cafekiosk.card.Service;

import com.zerobase.cafekiosk.card.dto.CardDto;

public interface CardService {

  void validate(Long paymentId, Long kioskId, String cardNumber);

  CardDto confirm(Long paymentId, Long kioskId, String cardNumber);
}
