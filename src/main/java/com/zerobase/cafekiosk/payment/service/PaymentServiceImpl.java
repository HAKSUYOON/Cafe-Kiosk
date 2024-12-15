package com.zerobase.cafekiosk.payment.service;

import com.zerobase.cafekiosk.admin.dto.RevenueDto;
import com.zerobase.cafekiosk.beverage.repository.BeverageRepository;
import com.zerobase.cafekiosk.cart.constant.CartStatus;
import com.zerobase.cafekiosk.cart.entity.Cart;
import com.zerobase.cafekiosk.cart.repository.CartRepository;
import com.zerobase.cafekiosk.member.entity.CustomUserDetails;
import com.zerobase.cafekiosk.member.entity.Member;
import com.zerobase.cafekiosk.member.repository.MemberRepository;
import com.zerobase.cafekiosk.order.constant.OrderStatus;
import com.zerobase.cafekiosk.order.entity.OrderEntity;
import com.zerobase.cafekiosk.order.repository.OrderRepository;
import com.zerobase.cafekiosk.payment.constant.PaymentMethod;
import com.zerobase.cafekiosk.payment.constant.PaymentStatus;
import com.zerobase.cafekiosk.payment.dto.PaymentDto;
import com.zerobase.cafekiosk.payment.entity.Payment;
import com.zerobase.cafekiosk.payment.model.PaymentInput;
import com.zerobase.cafekiosk.payment.repository.PaymentRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

  private final PaymentRepository paymentRepository;
  private final OrderRepository orderRepository;
  private final MemberRepository memberRepository;
  private final CartRepository cartRepository;
  private final BeverageRepository beverageRepository;

  @Override
  public PaymentDto createPayment(Long kioskId, Object principal) {

    OrderEntity orderEntity = orderRepository.findByKioskIdAndOrderStatus(
            kioskId, OrderStatus.ORDER_STATUS_ORDERED)
        .orElseThrow(() -> new RuntimeException("주문이 존재하지 않습니다."));

    String username;

    if (principal.equals("anonymousUser")) {
      username = "anonymousUser";
    } else {
      CustomUserDetails customUserDetails = (CustomUserDetails) principal;
      username = customUserDetails.getUsername();
    }

    Member member = memberRepository.findByUsername(username).orElseGet(Member::new);
    member.setUsername(username);

    if (paymentRepository.existsByOrderIdAndKioskIdAndPaymentStatus(orderEntity.getId(), kioskId,
        PaymentStatus.PAYMENT_STATUS_READY)) {
      throw new RuntimeException("이미 결제가 생성되어있습니다.");
    }

    boolean isSale = canSale(member);
    int usingStampCount = calculateUsingStampCount(member);
    int totalAmount = calculateTotalAmount(orderEntity);
    int saleAmount = calculateSaleAmount(orderEntity, member);
    int approvedAmount = Math.max(totalAmount - saleAmount, 0);

    Payment payment = Payment.buildPayment(kioskId, orderEntity, member, isSale, usingStampCount,
        totalAmount, saleAmount, approvedAmount);

    paymentRepository.save(payment);

    return PaymentDto.of(payment);
  }

  @Override
  @Transactional
  public PaymentDto confirm(Long paymentId, Long kioskId) {
    Payment payment = paymentRepository.findByIdAndKioskIdAndPaymentStatus(paymentId, kioskId,
            PaymentStatus.PAYMENT_STATUS_READY)
        .orElseThrow(() -> new RuntimeException("입력하신 결제번호에 대한 결제를 찾을 수 없습니다."));

    payment.setPaymentMethod(PaymentMethod.PAYMENT_METHOD_CARD);
    payment.setPaymentStatus(PaymentStatus.PAYMENT_STATUS_DONE);
    payment.setApprovedAt(LocalDateTime.now());
    paymentRepository.save(payment);

    OrderEntity orderEntity = orderRepository.findByIdAndKioskIdAndOrderStatus(payment.getOrderId(),
            kioskId, OrderStatus.ORDER_STATUS_ORDERED)
        .orElseThrow(() -> new RuntimeException("해당 주문이 존재하지 않습니다."));

    orderEntity.setOrderStatus(OrderStatus.ORDER_STATUS_PAID);
    orderRepository.save(orderEntity);

    List<Cart> cartList = orderEntity.getCartIdList().stream()
        .map(cartId -> cartRepository.findByIdAndKioskIdAndCartStatus(cartId, kioskId,
                CartStatus.CART_STATUS_ORDERED)
            .orElseThrow(() -> new RuntimeException("해당 장바구니가 존재하지 않습니다."))).collect(
            Collectors.toList());

    for (Cart cart : cartList) {
      cart.setCartStatus(CartStatus.CART_STATUS_PAID);
      cartRepository.save(cart);
    }

    return PaymentDto.of(payment);
  }

  @Override
  @Transactional
  public void setStamp(Long paymentId, PaymentInput request) {
    Payment payment = paymentRepository.findByIdAndKioskIdAndPaymentStatus(paymentId,
            request.getKioskId(),
            PaymentStatus.PAYMENT_STATUS_DONE)
        .orElseThrow(() -> new RuntimeException("입력하신 결제번호에 대한 적절한 결제정보를 가져오지 못했습니다."));

    if (!payment.getUsername().equals("anonymousUser")) {
      Member member = memberRepository.findByUsername(payment.getUsername())
          .orElseThrow(() -> new RuntimeException("해당 유저를 찾지 못했습니다."));
      saveChangeStampCount(member, payment);
    }
  }

  @Override
  public List<RevenueDto> todayRevenue() {

    List<Payment> payments = paymentRepository.findAllByPaymentStatusAndApprovedAtToday(
            PaymentStatus.PAYMENT_STATUS_DONE)
        .orElseThrow(() -> new RuntimeException("오늘의 매출액이 존재하지 않습니다."));

    return payments.stream().map(RevenueDto::of).collect(Collectors.toList());
  }

  @Override
  public List<RevenueDto> dateRevenue(String date) {

    LocalDateTime localDateTime = dateToLocalDateTime(date);

    List<Payment> payments = paymentRepository.findAllByPaymentStatusAndApprovedAtDate(
            PaymentStatus.PAYMENT_STATUS_DONE, localDateTime)
        .orElseThrow(() -> new RuntimeException("해당 날짜에 매출액이 존재하지 않습니다."));

    return payments.stream().map(RevenueDto::of).collect(Collectors.toList());
  }

  @Override
  public List<RevenueDto> monthRevenues(String month) {

    LocalDateTime localDateTime = monthToLocalDateTime(month);

    List<Payment> payments = paymentRepository.findAllByPaymentStatusAndApprovedAtMonth(
            PaymentStatus.PAYMENT_STATUS_DONE, localDateTime)
        .orElseThrow(() -> new RuntimeException("해당 월에 매출액이 존재하지 않습니다."));

    return payments.stream().map(RevenueDto::of).collect(Collectors.toList());
  }

  @Override
  public List<RevenueDto> yearRevenues(String year) {

    LocalDateTime localDateTime = yearToLocalDateTime(year);

    List<Payment> payments = paymentRepository.findAllByPaymentStatusAndApprovedAtYear(
            PaymentStatus.PAYMENT_STATUS_DONE, localDateTime)
        .orElseThrow(() -> new RuntimeException("해당 년도에 매출액이 존재하지 않습니다."));

    return payments.stream().map(RevenueDto::of).collect(Collectors.toList());
  }

  @Override
  public int calculateRevenue(List<RevenueDto> revenueDtoList) {
    return revenueDtoList.stream().map(RevenueDto::getApprovedAmount).reduce(0, Integer::sum);
  }

  @Override
  public int countRevenue(List<RevenueDto> revenueDtoList) {
    List<Long> paymentIdList = revenueDtoList.stream().map(RevenueDto::getId).collect(Collectors.toList());
    List<Payment> paymentList = paymentIdList.stream().map(it -> paymentRepository.findById(it)
        .orElseThrow(() -> new RuntimeException("해당 결제를 찾을 수 없습니다."))).collect(
        Collectors.toList());
    long count = paymentList.stream().filter(it -> !it.getUsername().equals("ADMIN")).count();

    return Optional.of(count).orElse(0L).intValue();
  }

  @Override
  public void tasting(Long paymentId) {
    Payment payment = paymentRepository.findByIdAndPaymentStatus(paymentId,
            PaymentStatus.PAYMENT_STATUS_READY)
        .orElseThrow(() -> new RuntimeException("입력하신 결제번호에 대한 적절한 결제정보를 가져오지 못했습니다."));

    paymentRepository.save(Payment.freePayment(payment));

    OrderEntity orderEntity = orderRepository.findByIdAndOrderStatus(payment.getOrderId(), OrderStatus.ORDER_STATUS_ORDERED)
        .orElseThrow(() -> new RuntimeException("해당 주문이 존재하지 않습니다."));

    orderEntity.setOrderStatus(OrderStatus.ORDER_STATUS_PAID);
    orderRepository.save(orderEntity);

    List<Cart> cartList = orderEntity.getCartIdList().stream()
        .map(cartId -> cartRepository.findByIdAndCartStatus(cartId, CartStatus.CART_STATUS_ORDERED)
            .orElseThrow(() -> new RuntimeException("해당 장바구니가 존재하지 않습니다."))).collect(
            Collectors.toList());

    for (Cart cart : cartList) {
      cart.setCartStatus(CartStatus.CART_STATUS_PAID);
      cartRepository.save(cart);
    }
  }

  private boolean canSale(Member member) {
    return member.getStamp() >= 10;
  }

  private int calculateUsingStampCount(Member member) {
    if (!canSale(member)) {
      return 0;
    }
    return member.getStamp() / 10;
  }

  private int calculateTotalAmount(OrderEntity orderEntity) {
    List<Cart> cartList = orderEntity.getCartIdList().stream().map(cartId ->
            cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("해당 장바구니가 없습니다.")))
        .collect(Collectors.toList());

    List<Integer> cartQuantityList = cartList.stream().map(Cart::getQuantity)
        .collect(Collectors.toList());

    List<Long> beverageIdList = cartList.stream().map(Cart::getBeverageId)
        .collect(Collectors.toList());

    List<Integer> priceList = beverageIdList.stream()
        .map(beverageId -> beverageRepository.findById(beverageId)
            .orElseThrow(() -> new RuntimeException("해당 음료가 존재하지않습니다.")).getPrice()).collect(
            Collectors.toList());

    int totalAmount = 0;
    for (int i = 0; i < cartQuantityList.size(); i++) {
      totalAmount += cartQuantityList.get(i) * priceList.get(i);
    }

    return totalAmount;
  }

  private int calculateSaleAmount(OrderEntity orderEntity, Member member) {
    if (!canSale(member)) {
      return 0;
    }

    int usingStampCount = calculateUsingStampCount(member);

    List<Integer> priceList = new ArrayList<>();

    List<Cart> cartList = orderEntity.getCartIdList().stream().map(cartId ->
            cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("해당 장바구니가 없습니다.")))
        .collect(Collectors.toList());

    List<Integer> quantityList = cartList.stream().map(Cart::getQuantity)
        .collect(Collectors.toList());

    List<Long> beverageIdList = cartList.stream().map(Cart::getBeverageId)
        .collect(Collectors.toList());

    List<Integer> beveragePriceList = beverageIdList.stream().map(beverageId ->
            beverageRepository.findById(beverageId)
                .orElseThrow(() -> new RuntimeException("해당 음료가 없습니다.")).getPrice())
        .collect(Collectors.toList());

    for (int i = 0; i < beveragePriceList.size(); i++) {
      for (int j = 0; j < quantityList.get(i); j++) {
        priceList.add(beveragePriceList.get(i));
      }
    }

    priceList.sort(Comparator.reverseOrder());

    int saleAmount = 0;

    for (int i = 0; i < usingStampCount; i++) {
      saleAmount += priceList.get(i);
    }

    return saleAmount;
  }

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  private synchronized void saveChangeStampCount(Member member, Payment payment) {
    member.setStamp(member.getStamp() - payment.getUsingStampCount() * 10);
    memberRepository.save(member);
  }

  private LocalDateTime dateToLocalDateTime(String date) {

    date = date.concat("000000");

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    return LocalDateTime.parse(date, formatter);
  }

  private LocalDateTime monthToLocalDateTime(String month) {

    month = month.concat("01000000");

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    return LocalDateTime.parse(month, formatter);
  }

  private LocalDateTime yearToLocalDateTime(String year) {

    year = year.concat("0101000000");

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    return LocalDateTime.parse(year, formatter);
  }
}
