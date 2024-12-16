package com.zerobase.cafekiosk.card.Service;

import com.zerobase.cafekiosk.card.constant.CardStatus;
import com.zerobase.cafekiosk.card.constant.CardType;
import com.zerobase.cafekiosk.card.dto.CardDto;
import com.zerobase.cafekiosk.card.entity.Card;
import com.zerobase.cafekiosk.card.repository.CardRepository;
import com.zerobase.cafekiosk.exception.Impl.CanceledCardException;
import com.zerobase.cafekiosk.exception.Impl.InsufficientBalanceException;
import com.zerobase.cafekiosk.exception.Impl.NotFoundCardException;
import com.zerobase.cafekiosk.exception.Impl.NotFoundPaymentException;
import com.zerobase.cafekiosk.exception.Impl.WrongCardNumberException;
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
      throw new WrongCardNumberException();
    }

    Card card = cardRepository.findByCardNumber(cardNumber)
        .orElseThrow(NotFoundCardException::new);

    if (card.getCardStatus().equals(CardStatus.CARD_STATUS_STOP)) {
      throw new CanceledCardException();
    }

    if (card.getCardType().equals(CardType.CARD_TYPE_DEBIT)) {
      int approvedAmount = paymentRepository.findByIdAndKioskIdAndPaymentStatus(paymentId, kioskId,
              PaymentStatus.PAYMENT_STATUS_READY)
          .orElseThrow(NotFoundPaymentException::new)
          .getApprovedAmount();
      if (card.getBalance() < approvedAmount) {
        throw new InsufficientBalanceException();
      }
    }
  }

  @Override
  @Transactional
  public CardDto confirm(Long paymentId, Long kioskId, String cardNumber) {
    Card card = cardRepository.findByCardNumber(cardNumber)
        .orElseThrow(NotFoundCardException::new);

    int approvedAmount = paymentRepository.findByIdAndKioskIdAndPaymentStatus(paymentId, kioskId,
            PaymentStatus.PAYMENT_STATUS_READY)
        .orElseThrow(NotFoundPaymentException::new)
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
