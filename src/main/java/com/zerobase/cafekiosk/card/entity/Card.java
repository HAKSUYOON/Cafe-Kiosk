package com.zerobase.cafekiosk.card.entity;

import com.zerobase.cafekiosk.card.constant.CardStatus;
import com.zerobase.cafekiosk.card.constant.CardType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Card {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String cardNumber;

  @PositiveOrZero
  private int Balance;

  private CardType cardType;

  private CardStatus cardStatus;

}
