package com.zerobase.cafekiosk.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zerobase.cafekiosk.member.model.SignupInput;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;

  @JsonIgnore
  private String password;

  @ElementCollection(fetch = FetchType.EAGER)
  private List<String> roles;

  private int stamp;

  public Member buildMember(SignupInput request) {
    List<String> roles = new ArrayList<>();
    roles.add("USER");
    return Member.builder()
        .username(request.getUsername())
        .password(request.getPassword())
        .roles(roles)
        .stamp(0)
        .build();
  }
}
