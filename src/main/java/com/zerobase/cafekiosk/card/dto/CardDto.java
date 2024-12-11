package com.zerobase.cafekiosk.card.dto;

import com.zerobase.cafekiosk.card.constant.CardType;
import com.zerobase.cafekiosk.card.entity.Card;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {

  private String cardNumber;
  private int approvedAmount;
  @PositiveOrZero
  private int balance;
  private CardType cardType;

  public CardDto setCardDto(CardDto cardDto, Card card, int approvedAmount) {
    cardDto.setCardNumber(card.getCardNumber());
    cardDto.setApprovedAmount(approvedAmount);
    cardDto.setBalance(card.getBalance());
    cardDto.setCardType(card.getCardType());
    return cardDto;
  }
}
