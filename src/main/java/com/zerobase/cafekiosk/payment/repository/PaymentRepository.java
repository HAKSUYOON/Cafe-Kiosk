package com.zerobase.cafekiosk.payment.repository;

import com.zerobase.cafekiosk.payment.constant.PaymentStatus;
import com.zerobase.cafekiosk.payment.entity.Payment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

  Optional<Payment> findByIdAndKioskIdAndPaymentStatus(Long paymentId, Long kioskId, PaymentStatus paymentStatus);

  boolean existsByOrderIdAndKioskIdAndPaymentStatus(Long id, Long kioskId, PaymentStatus paymentStatus);

  @Query(value = "SELECT p FROM Payment p WHERE p.paymentStatus = ?1 AND FUNCTION('DATE_FORMAT', p.approvedAt, '%Y.%m.%d') = FUNCTION('DATE_FORMAT', current_timestamp, '%Y.%m.%d')")
  Optional<List<Payment>> findAllByPaymentStatusAndApprovedAtToday(PaymentStatus paymentStatus);

  @Query(value = "SELECT p FROM Payment p WHERE p.paymentStatus = ?1 AND FUNCTION('DATE_FORMAT', p.approvedAt, '%Y.%m.%d') = FUNCTION('DATE_FORMAT', ?2, '%Y.%m.%d')")
  Optional<List<Payment>> findAllByPaymentStatusAndApprovedAtDate(PaymentStatus paymentStatus, LocalDateTime localDateTime);

  @Query(value = "SELECT p FROM Payment p WHERE p.paymentStatus = ?1 AND FUNCTION('DATE_FORMAT', p.approvedAt, '%Y.%m') = FUNCTION('DATE_FORMAT', ?2, '%Y.%m')")
  Optional<List<Payment>> findAllByPaymentStatusAndApprovedAtMonth(PaymentStatus paymentStatus, LocalDateTime localDateTime);

  @Query(value = "SELECT p FROM Payment p WHERE p.paymentStatus = ?1 AND FUNCTION('DATE_FORMAT', p.approvedAt, '%Y') = FUNCTION('DATE_FORMAT', ?2, '%Y')")
  Optional<List<Payment>> findAllByPaymentStatusAndApprovedAtYear(PaymentStatus paymentStatus, LocalDateTime localDateTime);

  Optional<Payment> findByIdAndPaymentStatus(Long paymentId, PaymentStatus paymentStatus);
}
