package com.zerobase.cafekiosk.admin.controller;

import com.zerobase.cafekiosk.admin.dto.RevenueDto;
import com.zerobase.cafekiosk.admin.dto.StampHistoryDto;
import com.zerobase.cafekiosk.admin.model.RevenueResult;
import com.zerobase.cafekiosk.admin.service.StampHistoryService;
import com.zerobase.cafekiosk.payment.service.PaymentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

  private final PaymentService paymentService;
  private final StampHistoryService stampHistoryService;

  @GetMapping("/revenues/today")
  public ResponseEntity<?> todayRevenue() {

    List<RevenueDto> revenueDtoList = paymentService.todayRevenue();
    int revenue = paymentService.calculateRevenue(revenueDtoList);
    int count = paymentService.countRevenue(revenueDtoList);

    return ResponseEntity.ok(new RevenueResult<>(revenueDtoList, revenue, count));
  }

  @GetMapping("/revenues/date")
  public ResponseEntity<?> dateRevenue(@RequestParam String date) {

    List<RevenueDto> revenueDtoList = paymentService.dateRevenue(date);
    int revenue = paymentService.calculateRevenue(revenueDtoList);
    int count = paymentService.countRevenue(revenueDtoList);

    return ResponseEntity.ok(new RevenueResult<>(revenueDtoList, revenue, count));
  }

  @GetMapping("/revenues/month")
  public ResponseEntity<?> monthRevenues(@RequestParam String month) {

    List<RevenueDto> revenueDtoList = paymentService.monthRevenues(month);
    int revenue = paymentService.calculateRevenue(revenueDtoList);
    int count = paymentService.countRevenue(revenueDtoList);

    return ResponseEntity.ok(new RevenueResult<>(revenueDtoList, revenue, count));
  }

  @GetMapping("/revenues/year")
  public ResponseEntity<?> yearRevenues(@RequestParam String year) {

    List<RevenueDto> revenueDtoList = paymentService.yearRevenues(year);
    int revenue = paymentService.calculateRevenue(revenueDtoList);
    int count = paymentService.countRevenue(revenueDtoList);

    return ResponseEntity.ok(new RevenueResult<>(revenueDtoList, revenue, count));
  }

  @GetMapping("/stamp-histories")
  public ResponseEntity<?> stampHistories() {

    List<StampHistoryDto> stampHistoryDtoList = stampHistoryService.list();

    return ResponseEntity.ok(stampHistoryDtoList);
  }

  @GetMapping("/stamp-histories/{username}")
  public ResponseEntity<?> stampHistoriesByUsername(@PathVariable String username) {

    List<StampHistoryDto> stampHistoryDtoList = stampHistoryService.listByUsername(username);

    return ResponseEntity.ok(stampHistoryDtoList);
  }

  @PutMapping("/tasting/{paymentId}")
  public void tasting(@PathVariable Long paymentId) {

    paymentService.tasting(paymentId);
  }
}
