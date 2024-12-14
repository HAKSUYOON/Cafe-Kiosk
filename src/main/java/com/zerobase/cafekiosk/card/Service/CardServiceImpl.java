package com.zerobase.cafekiosk.card.Service;

import com.zerobase.cafekiosk.card.constant.CardStatus;
import com.zerobase.cafekiosk.card.constant.CardType;
import com.zerobase.cafekiosk.card.dto.CardDto;
import com.zerobase.cafekiosk.card.entity.Card;
import com.zerobase.cafekiosk.card.repository.CardRepository;
import com.zerobase.cafekiosk.payment.constant.PaymentStatus;
import com.zerobase.cafekiosk.payment.repository.PaymentRepository;
import javax.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CardServiceImpl implements CardService {

  private final CardRepository cardRepository;
  private final PaymentRepository paymentRepository;

  @Override
  public void validate(Long paymentId, Long kioskId, String cardNumber) {

    if (cardNumber.replace("_", "").trim().length() != 16) {
      throw new RuntimeException("카드번호가 유효하지 않습니다.");
    }

    Card card = cardRepository.findByCardNumber(cardNumber)
        .orElseThrow(() -> new RuntimeException("인증되지 않은 카드입니다."));

    if (card.getCardStatus().equals(CardStatus.CARD_STATUS_STOP)) {
      throw new RuntimeException("해당 카드는 정지된 카드입니다. 다른 카드를 사용하세요.");
    }

    if (card.getCardType().equals(CardType.CARD_TYPE_DEBIT)) {
      int approvedAmount = paymentRepository.findByIdAndKioskIdAndPaymentStatus(paymentId, kioskId,
              PaymentStatus.PAYMENT_STATUS_READY)
          .orElseThrow(() -> new RuntimeException("입력하신 결제번호에 대한 결제를 찾을 수 없습니다."))
          .getApprovedAmount();
      if (card.getBalance() < approvedAmount) {
        throw new RuntimeException("잔액이 부족합니다.");
      }
    }
  }

  @Override
  @Transactional
  public CardDto confirm(Long paymentId, Long kioskId, String cardNumber) {
    Card card = cardRepository.findByCardNumber(cardNumber)
        .orElseThrow(() -> new RuntimeException("인증되지 않은 카드입니다."));

    int approvedAmount = paymentRepository.findByIdAndKioskIdAndPaymentStatus(paymentId, kioskId,
            PaymentStatus.PAYMENT_STATUS_READY)
        .orElseThrow(() -> new RuntimeException("입력하신 결제번호에 대한 결제를 찾을 수 없습니다."))
        .getApprovedAmount();

    CardDto cardDto = new CardDto();

    if (card.getCardType() == CardType.CARD_TYPE_CREDIT) {
      cardDto.setCardDto(cardDto, card, approvedAmount);
    } else {
      saveChangeBalance(card, card.getBalance(), approvedAmount);
      cardDto.setCardDto(cardDto, card, approvedAmount);
    }

    return cardDto;
  }

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  private synchronized void saveChangeBalance(Card card, int balance, int approvedAmount) {
    int changeBalance = balance - approvedAmount;
    card.setBalance(changeBalance);
    cardRepository.save(card);

  }
}
