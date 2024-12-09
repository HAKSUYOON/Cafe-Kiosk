package com.zerobase.cafekiosk.member.Service;

import com.zerobase.cafekiosk.member.dto.MemberDetailDto;
import com.zerobase.cafekiosk.member.dto.MemberDto;
import com.zerobase.cafekiosk.member.entity.Member;
import com.zerobase.cafekiosk.member.model.SigninInput;
import com.zerobase.cafekiosk.member.model.SignupInput;
import java.security.Principal;

public interface MemberService {

  MemberDto register(SignupInput request);

  Member authenticate(SigninInput request);

  MemberDetailDto detail(Principal principal);
}
