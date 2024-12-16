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

  /**
   * 오늘 매출액 조회
   */
  @GetMapping("/revenues/today")
  public ResponseEntity<?> todayRevenue() {

    List<RevenueDto> revenueDtoList = paymentService.todayRevenue();
    int revenue = paymentService.calculateRevenue(revenueDtoList);
    int count = paymentService.countRevenue(revenueDtoList);

    return ResponseEntity.ok(new RevenueResult<>(revenueDtoList, revenue, count));
  }

  /**
   * 해당 날짜의 매출액 조회
   * 파라미터 : 날짜 (yyyyMMdd)
   */
  @GetMapping("/revenues/date")
  public ResponseEntity<?> dateRevenue(@RequestParam String date) {

    List<RevenueDto> revenueDtoList = paymentService.dateRevenue(date);
    int revenue = paymentService.calculateRevenue(revenueDtoList);
    int count = paymentService.countRevenue(revenueDtoList);

    return ResponseEntity.ok(new RevenueResult<>(revenueDtoList, revenue, count));
  }

  /**
   * 해당 월의 매출액 조회
   * 파라미터 : 날짜 (yyyyMM)
   */
  @GetMapping("/revenues/month")
  public ResponseEntity<?> monthRevenues(@RequestParam String month) {

    List<RevenueDto> revenueDtoList = paymentService.monthRevenues(month);
    int revenue = paymentService.calculateRevenue(revenueDtoList);
    int count = paymentService.countRevenue(revenueDtoList);

    return ResponseEntity.ok(new RevenueResult<>(revenueDtoList, revenue, count));
  }

  /**
   * 해당 년도의 매출액 조회
   * 파라미터 : 날짜 (yyyy)
   */
  @GetMapping("/revenues/year")
  public ResponseEntity<?> yearRevenues(@RequestParam String year) {

    List<RevenueDto> revenueDtoList = paymentService.yearRevenues(year);
    int revenue = paymentService.calculateRevenue(revenueDtoList);
    int count = paymentService.countRevenue(revenueDtoList);

    return ResponseEntity.ok(new RevenueResult<>(revenueDtoList, revenue, count));
  }

  /**
   * 스탬프 사용이력 조회
   */
  @GetMapping("/stamp-histories")
  public ResponseEntity<?> stampHistories() {

    List<StampHistoryDto> stampHistoryDtoList = stampHistoryService.list();

    return ResponseEntity.ok(stampHistoryDtoList);
  }

  /**
   * 회원아이디로 스탬프 사용이력 조회
   * 매개변수 : 회원아이디
   */
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
