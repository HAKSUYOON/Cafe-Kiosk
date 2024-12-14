package com.zerobase.cafekiosk.payment.service;

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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
}
