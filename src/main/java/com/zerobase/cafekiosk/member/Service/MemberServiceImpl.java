package com.zerobase.cafekiosk.member.Service;

import com.zerobase.cafekiosk.member.dto.MemberDetailDto;
import com.zerobase.cafekiosk.member.dto.MemberDto;
import com.zerobase.cafekiosk.member.entity.Member;
import com.zerobase.cafekiosk.member.model.SigninInput;
import com.zerobase.cafekiosk.member.model.SignupInput;
import com.zerobase.cafekiosk.member.repository.MemberRepository;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService, UserDetailsService {

  private final PasswordEncoder passwordEncoder;
  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return memberRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다."));
  }

  @Override
  public MemberDto register(SignupInput request) {
    if (memberRepository.existsByUsername(request.getUsername())) {
      throw new RuntimeException("이미 존재하는 회원이름입니다.");
    }

    request.setPassword(passwordEncoder.encode(request.getPassword()));
    Member member = new Member().buildMember(request);
    memberRepository.save(member);
    return MemberDto.of(member);
  }

  @Override
  public Member authenticate(SigninInput request) {
    Member member = memberRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new RuntimeException("회원이름이 존재하지 않습니다."));

    if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
      throw new RuntimeException("비밀번호가 일치하지 않습니다.");
    }

    return member;
  }

  @Override
  public MemberDetailDto detail(Principal principal) {

    Member member = memberRepository.findByUsername(principal.getName())
        .orElseThrow(() -> new RuntimeException("로그인 정보를 확인하세요."));
    return MemberDetailDto.of(member);
  }
}
