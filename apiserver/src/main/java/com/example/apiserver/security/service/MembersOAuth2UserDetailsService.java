package com.example.apiserver.security.service;

import com.example.apiserver.entity.Members;
import com.example.apiserver.entity.MembersRole;
import com.example.apiserver.repository.MembersRepository;
import com.example.apiserver.security.dto.MembersAuthDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class MembersOAuth2UserDetailsService extends DefaultOAuth2UserService {
  private final MembersRepository membersRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate =
        new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);

    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    SocialType socialType = getSocialType(registrationId.trim().toString());
    String userNameAttributeName = userRequest.getClientRegistration()
        .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
    log.info("userNameAttributeName >> " + userNameAttributeName);
    Map<String, Object> attributes = oAuth2User.getAttributes();
    for (Map.Entry<String, Object> entry : attributes.entrySet()) {
      System.out.println(entry.getKey() + ":" + entry.getValue());
    }
    String email = null;
    if (socialType.name().equals("GOOGLE"))
      email = oAuth2User.getAttribute("email");
    log.info("Email: " + email);
    Members members = saveSocialMember(email);

    MembersAuthDTO membersAuthDTO = new MembersAuthDTO(
        members.getEmail(),
        members.getPassword(),
        true,
        members.getRoleSet().stream().map(
                role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
            .collect(Collectors.toList())
        , attributes
    );
    membersAuthDTO.setFromSocial(members.isFromSocial());
    membersAuthDTO.setName(members.getName());
    log.info("membersAuthDTO: " + membersAuthDTO);
    return membersAuthDTO;
  }

  private Members saveSocialMember(String email) {
    Optional<Members> result = membersRepository.findByEmail(email, true);
    if (result.isPresent()) return result.get();

    Members members = Members.builder()
        .email(email)
        .password(passwordEncoder.encode("1"))
        .fromSocial(true)
        .build();
    members.addMemberRole(MembersRole.USER);
    membersRepository.save(members);
    return members;
  }

  private SocialType getSocialType(String registrationId) {
    if (SocialType.NAVER.name().equals(registrationId)) {
      return SocialType.NAVER;
    }
    if (SocialType.KAKAO.name().equals(registrationId)) {
      return SocialType.KAKAO;
    }
    return SocialType.GOOGLE;
  }

  enum SocialType {
    KAKAO, NAVER, GOOGLE
  }
}
