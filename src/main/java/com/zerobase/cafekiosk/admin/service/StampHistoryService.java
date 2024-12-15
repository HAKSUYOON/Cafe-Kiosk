package com.zerobase.cafekiosk.admin.service;

import com.zerobase.cafekiosk.admin.dto.StampHistoryDto;
import com.zerobase.cafekiosk.payment.model.PaymentInput;
import java.util.List;

public interface StampHistoryService {

  List<StampHistoryDto> list();

  List<StampHistoryDto> listByUsername(String username);

  void saveHistory(Long paymentId, PaymentInput request);
}
