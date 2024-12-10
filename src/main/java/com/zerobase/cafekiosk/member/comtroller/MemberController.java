package com.zerobase.cafekiosk.member.comtroller;

import com.zerobase.cafekiosk.config.TokenProvider;
import com.zerobase.cafekiosk.member.Service.MemberService;
import com.zerobase.cafekiosk.member.dto.MemberDetailDto;
import com.zerobase.cafekiosk.member.dto.MemberDto;
import com.zerobase.cafekiosk.member.entity.Member;
import com.zerobase.cafekiosk.member.model.SigninInput;
import com.zerobase.cafekiosk.member.model.SignupInput;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;
  private final TokenProvider tokenProvider;

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody SignupInput request) {
    MemberDto memberDto = memberService.register(request);
    return ResponseEntity.ok(memberDto);
  }

  @PostMapping("/signin")
  public ResponseEntity<?> signin(@RequestBody SigninInput request) {
    Member member = memberService.authenticate(request);
    String token = tokenProvider.generateToken(member.getUsername(), member.getRoles());
    return ResponseEntity.ok(token);
  }

  @GetMapping("/detail")
  public ResponseEntity<?> detail(Principal principal) {
    MemberDetailDto memberDetailDto = memberService.detail(principal);
    return ResponseEntity.ok(memberDetailDto);
  }


}
