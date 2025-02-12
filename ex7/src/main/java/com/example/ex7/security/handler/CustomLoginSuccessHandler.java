package com.example.ex7.security.handler;

import com.example.ex7.security.dto.ClubMemberAuthDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Log4j2
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
  private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
  private PasswordEncoder passwordEncoder;

  public CustomLoginSuccessHandler(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Authentication authentication)
      throws IOException, ServletException {

    log.info("onAuthenticationSuccess -------------------------------");
    ClubMemberAuthDTO clubMemberAuthDTO = (ClubMemberAuthDTO) authentication.getPrincipal();
    // social로 첫 로그인한 경우, 회원정보를 간단히 수정창으로 이동하기 위함.
    if (clubMemberAuthDTO.isFromSocial()
        && passwordEncoder.matches("1", clubMemberAuthDTO.getPassword())) {
      redirectStrategy.sendRedirect(request, response, "/sample/modify");
      return;
    }

    Collection<GrantedAuthority> authors =
        (Collection<GrantedAuthority>) clubMemberAuthDTO.getAuthorities();
    List<String> result = authors.stream().map(new Function<GrantedAuthority, String>() {
      @Override
      public String apply(GrantedAuthority grantedAuthority) {
        return grantedAuthority.getAuthority();
      }
    }).collect(Collectors.toList());
    System.out.println(">>" + result.toString());
    for (int i = 0; i < result.size(); i++) {
      if (result.get(i).equals("ROLE_ADMIN")) {
        response.sendRedirect(request.getContextPath() + "/sample/admin");
      } else if (result.get(i).equals("ROLE_MANAGER")) {
        response.sendRedirect(request.getContextPath() + "/sample/manager");
      } else {
        response.sendRedirect(request.getContextPath() + "/sample/all");
      }
      break;
    }
  }
}
