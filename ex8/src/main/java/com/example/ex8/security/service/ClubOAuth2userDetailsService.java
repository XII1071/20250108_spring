package com.example.ex8.security.service;

import com.example.ex8.entity.ClubMember;
import com.example.ex8.entity.ClubMemberRole;
import com.example.ex8.repository.ClubMemberRepository;
import com.example.ex8.security.dto.ClubMemberAuthDTO;
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
public class ClubOAuth2userDetailsService extends DefaultOAuth2UserService {
  private final ClubMemberRepository clubMemberRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    log.info("============= userRequest: " + userRequest);
    // OAuth2UserService는 social로부터 정보를 받기 위한 객체 생성
    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate =
        new DefaultOAuth2UserService();
    //delegate.loadUser()는 userRequest(소셜에서 온 유저정보)를 세션 객체(OAuth2User)로 변환
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
    ClubMember clubMember = saveSocialMember(email);
    log.info("clubMember: " + clubMember);
    ClubMemberAuthDTO clubMemberAuthDTO = new ClubMemberAuthDTO(
        clubMember.getEmail(),
        clubMember.getPassword(),
        true,
        clubMember.getRoleSet().stream().map(
                role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
            .collect(Collectors.toList())
        , attributes
    );
    clubMemberAuthDTO.setFromSocial(clubMember.isFromSocial());
    clubMemberAuthDTO.setName(clubMember.getName());
    log.info("clubMemberAuthDTO: " + clubMemberAuthDTO);
    return clubMemberAuthDTO;
  }

  private ClubMember saveSocialMember(String email) {
    Optional<ClubMember> result = clubMemberRepository.findByEmail(email, true);
    if (result.isPresent()) return result.get();

    // 소셜에서 넘어온 정보가 DB에 없을 때 저장하는 부분
    ClubMember clubMember = ClubMember.builder()
        .email(email)
        .password(passwordEncoder.encode("1"))
        .fromSocial(true)
        .build();
    clubMember.addMemberRole(ClubMemberRole.USER);
    clubMemberRepository.save(clubMember);
    return clubMember;
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
