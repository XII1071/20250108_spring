package com.example.apiserver.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@ToString
public class MembersAuthDTO extends User implements OAuth2User {
  private String email;
  private String name;
  private boolean fromSocial;
  private String password;
  private Map<String, Object> attr; //소셜로부터 받은 정보를 저장하는 속성

  public MembersAuthDTO(String username, String password,
                        Collection<? extends GrantedAuthority> authorities, String email,
                        String name, boolean fromSocial) {
    super(username, password, authorities);
    this.email = email;
    this.password = password;
    this.name = name;
    this.fromSocial = fromSocial;
  }

  // 소셜로부터 받을 때 별도의 생성자
  public MembersAuthDTO(String username, String password,
                        boolean fromSocial,
                        Collection<? extends GrantedAuthority> authorities,
                        Map<String, Object> attr) {
    this(username, password, authorities, username, username, fromSocial);
    this.attr = attr;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return this.attr;
  }
}
