package com.zerobase.cafekiosk.member.Service;

import com.zerobase.cafekiosk.member.entity.Member;
import com.zerobase.cafekiosk.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return memberRepository.findByUsername(username).map(this::createUserDetails)
        .orElseThrow(() -> new UsernameNotFoundException("해당 회원을 찾을 수 없습니다."));
  }

  private UserDetails createUserDetails(Member member) {
    return User.builder()
        .username(member.getUsername())
        .password(passwordEncoder.encode(member.getPassword()))
        .roles(member.getRoles().toArray(new String[0]))
        .build();
  }
}
