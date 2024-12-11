package com.zerobase.cafekiosk.card.repository;

import com.zerobase.cafekiosk.card.entity.Card;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {

  Optional<Card> findByCardNumber(String cardNumber);
}
