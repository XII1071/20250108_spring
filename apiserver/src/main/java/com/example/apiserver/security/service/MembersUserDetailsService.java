package com.example.apiserver.security.service;

import com.example.apiserver.entity.Members;
import com.example.apiserver.repository.MembersRepository;
import com.example.apiserver.security.dto.MembersAuthDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
public class MembersUserDetailsService implements UserDetailsService {
  private final MembersRepository membersRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Members> result = membersRepository.findByEmail(username, false);
    if (!result.isPresent()) throw new UsernameNotFoundException("Check Email or Social");
    Members members = result.get();

    MembersAuthDTO membersAuthDTO = new MembersAuthDTO(
        members.getEmail(), members.getPassword(),
        members.getRoleSet().stream().map(
            membersRole -> new SimpleGrantedAuthority(
                "ROLE_" + membersRole.name()
            )
        ).collect(Collectors.toList()),
        members.getEmail(),
        members.getName(), members.isFromSocial()
    );
    return membersAuthDTO;
  }
}
