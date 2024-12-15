package com.zerobase.cafekiosk.admin.controller;

import com.zerobase.cafekiosk.admin.dto.RevenueDto;
import com.zerobase.cafekiosk.payment.service.PaymentService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

  private final PaymentService paymentService;

  @GetMapping("/revenues/today")
  public ResponseEntity<?> todayRevenue() {

    List<RevenueDto> revenueDtoList = paymentService.todayRevenue();

    return ResponseEntity.ok(revenueDtoList);
  }

  @GetMapping("/revenues/date")
  public ResponseEntity<?> dateRevenue(@RequestParam String date) {

    List<RevenueDto> revenueDtoList = paymentService.dateRevenue(date);

    return ResponseEntity.ok(revenueDtoList);
  }

  @GetMapping("/revenues/month")
  private ResponseEntity<?> monthRevenues(@RequestParam String month) {

    List<RevenueDto> revenueDtoList = paymentService.monthRevenues(month);

    return ResponseEntity.ok(revenueDtoList);
  }

  @GetMapping("/revenues/year")
  private ResponseEntity<?> yearRevenues(@RequestParam String year) {

    List<RevenueDto> revenueDtoList = paymentService.yearRevenues(year);

    return ResponseEntity.ok(revenueDtoList);
  }
}