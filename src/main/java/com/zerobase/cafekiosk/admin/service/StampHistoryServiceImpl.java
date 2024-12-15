package com.zerobase.cafekiosk.admin.service;

import com.zerobase.cafekiosk.admin.dto.StampHistoryDto;
import com.zerobase.cafekiosk.admin.entity.StampHistory;
import com.zerobase.cafekiosk.admin.repository.StampHistoryRepository;
import com.zerobase.cafekiosk.member.entity.Member;
import com.zerobase.cafekiosk.member.repository.MemberRepository;
import com.zerobase.cafekiosk.payment.constant.PaymentStatus;
import com.zerobase.cafekiosk.payment.entity.Payment;
import com.zerobase.cafekiosk.payment.model.PaymentInput;
import com.zerobase.cafekiosk.payment.repository.PaymentRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StampHistoryServiceImpl implements StampHistoryService {

  private final StampHistoryRepository stampHistoryRepository;
  private final PaymentRepository paymentRepository;
  private final MemberRepository memberRepository;

  @Override
  public List<StampHistoryDto> list() {

    List<StampHistory> stampHistories = stampHistoryRepository.findAll();

    return stampHistories.stream().map(StampHistoryDto::of).collect(Collectors.toList());
  }

  @Override
  public List<StampHistoryDto> listByUsername(String username) {

    List<StampHistory> stampHistories = stampHistoryRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("해당 유저의 스탬프이력이 없습니다."));

    return stampHistories.stream().map(StampHistoryDto::of).collect(Collectors.toList());
  }

  @Override
  public void saveHistory(Long paymentId, PaymentInput request) {

    Payment payment = paymentRepository.findByIdAndKioskIdAndPaymentStatus(
            paymentId, request.getKioskId(), PaymentStatus.PAYMENT_STATUS_DONE)
        .orElseThrow(() -> new RuntimeException("입력하신 결제번호에 대한 적절한 결제정보를 가져오지 못했습니다."));

    if (!payment.getUsername().equals("AnonymousUser")) {
      StampHistory stampHistory = StampHistory.buildStampHistory(payment,
          afterStamp(payment.getUsername()),
          beforeStamp(payment.getUsingStampCount(), payment.getUsername()));
      stampHistoryRepository.save(stampHistory);
    }
  }

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  private synchronized int afterStamp(String username) {
    Member member = memberRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

    return member.getStamp();
  }

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  private synchronized int beforeStamp(int usingStampCount, String username) {
    return afterStamp(username) + (usingStampCount * 10);
  }
}
