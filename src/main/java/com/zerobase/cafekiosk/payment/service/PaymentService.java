package com.zerobase.cafekiosk.payment.service;

import com.zerobase.cafekiosk.admin.dto.RevenueDto;
import com.zerobase.cafekiosk.payment.dto.PaymentDto;
import com.zerobase.cafekiosk.payment.model.PaymentInput;
import java.util.List;

public interface PaymentService {

  PaymentDto createPayment(Long kioskId, Object principal);

  PaymentDto confirm(Long paymentId, Long kioskId);

  void setStamp(Long paymentId, PaymentInput request);

  List<RevenueDto> todayRevenue();

  List<RevenueDto> dateRevenue(String date);

  List<RevenueDto> monthRevenues(String month);

  List<RevenueDto> yearRevenues(String year);

  int calculateRevenue(List<RevenueDto> revenueDtoList);

  int countRevenue(List<RevenueDto> revenueDtoList);

  void tasting(Long paymentId);
}
