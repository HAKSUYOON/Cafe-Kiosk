package com.zerobase.cafekiosk.payment.repository;

import com.zerobase.cafekiosk.payment.constant.PaymentStatus;
import com.zerobase.cafekiosk.payment.entity.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

  Optional<Payment> findByIdAndKioskIdAndPaymentStatus(Long paymentId, Long kioskId, PaymentStatus paymentStatus);

  boolean existsByOrderIdAndKioskIdAndPaymentStatus(Long id, Long kioskId, PaymentStatus paymentStatus);
}
