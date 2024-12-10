package com.zerobase.cafekiosk.member.entity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Data
@Builder
public class CustomUserDetails implements UserDetails {

  private Long id;
  private String username;
  private String password;
  private List<String> roles;
  private int stamp;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public static CustomUserDetails of(Member member) {
    return CustomUserDetails.builder()
        .id(member.getId())
        .username(member.getUsername())
        .password(member.getPassword())
        .roles(member.getRoles())
        .stamp(member.getStamp())
        .build();
  }

}
