package com.zerobase.cafekiosk.payment.controller;

import com.zerobase.cafekiosk.card.Service.CardService;
import com.zerobase.cafekiosk.card.dto.CardDto;
import com.zerobase.cafekiosk.payment.dto.PaymentDto;
import com.zerobase.cafekiosk.payment.model.PaymentInput;
import com.zerobase.cafekiosk.payment.model.PaymentResult;
import com.zerobase.cafekiosk.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/main/payment")
public class PaymentController {

  private final PaymentService paymentService;
  private final CardService cardService;

  @PostMapping
  public ResponseEntity<?> createPayment(@RequestBody Long kioskId) {

    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    PaymentDto paymentDto = paymentService.createPayment(kioskId, principal);

    return ResponseEntity.ok(paymentDto);
  }

  @PostMapping("/confirm/{paymentId}")
  public ResponseEntity<?> confirmPayment(@PathVariable Long paymentId,
      @RequestBody PaymentInput request) {

    cardService.validate(paymentId, request.getKioskId(), request.getCardNumber());
    CardDto cardDto = cardService.confirm(paymentId, request.getKioskId(), request.getCardNumber());
    PaymentDto paymentDto = paymentService.confirm(paymentId, request.getKioskId());

    return ResponseEntity.ok().body(new PaymentResult<>(paymentDto, cardDto));
  }
}
