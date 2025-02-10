package com.example.ex7.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
@ToString
public class ClubMemberAuthDTO extends User {
  private String email;
  private String name;
  private boolean fromSocial;

  public ClubMemberAuthDTO(String username, String password,
                           Collection<? extends GrantedAuthority> authorities,
                           String email, String name, boolean fromSocial) {
    super(username, password, authorities);
    this.email = email;
    this.name = name;
    this.fromSocial = fromSocial;
  }
}
