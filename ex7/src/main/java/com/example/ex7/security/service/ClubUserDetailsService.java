package com.example.ex7.security.service;

import com.example.ex7.entity.ClubMember;
import com.example.ex7.repository.ClubMemberRepository;
import com.example.ex7.security.dto.ClubMemberAuthDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ClubUserDetailsService implements UserDetailsService {
  private final ClubMemberRepository clubMemberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<ClubMember> result = clubMemberRepository.findByEmail(username, false);
    if(!result.isPresent()) throw new UsernameNotFoundException("Check Email or Social");
    ClubMember clubMember = result.get();

    // session으로 저장하기 위한 dto 객체
    ClubMemberAuthDTO clubMemberAuthDTO = new ClubMemberAuthDTO(
        clubMember.getEmail(), clubMember.getPassword(),
        clubMember.getRoleSet().stream().map(
            clubMemberRole -> new SimpleGrantedAuthority(
                // 스프링시큐리티의 내부에서 'USER'라는 단어를 상수처럼
                // 인증된 사용자의 의미로 사용하기 때문에 붙여 사용(USER == ROLE_USER)
                // ROLE_ 는 스프링 시큐리티에서 역할을 명확히 구분, 역할에 대한 권한을 설정
                "ROLE_" + clubMemberRole.name()
            )
        ).collect(Collectors.toList()),
        clubMember.getEmail(),
        clubMember.getName(), clubMember.isFromSocial()
    );
    return clubMemberAuthDTO;
  }
}
