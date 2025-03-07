package com.example.apiserver.service;

import com.example.apiserver.dto.MembersDTO;
import com.example.apiserver.entity.Members;
import com.example.apiserver.entity.MembersRole;

import java.util.function.Function;
import java.util.stream.Collectors;

public interface MembersService {
  Long registerMembers(MembersDTO membersDTO);
  Long updateMembers(MembersDTO membersDTO);
  void removeMembers(Long mid);
  MembersDTO getMembers(Long mid);
  MembersDTO loginCheck(String email);

  default Members dtoToEnitity(MembersDTO membersDTO) {
    Members members = Members.builder()
        .mid(membersDTO.getMid())
        .email(membersDTO.getEmail())
        .password(membersDTO.getPassword())
        .nickname(membersDTO.getNickname())
        .name(membersDTO.getName())
        .mobile(membersDTO.getMobile())
        .fromSocial(membersDTO.isFromSocial())
        .roleSet(membersDTO.getRoleSet().stream().map(new Function<String, MembersRole>() {
          @Override
          public MembersRole apply(String str) {
            if (str.equals("ROLE_USER")) return MembersRole.USER;
            else if (str.equals("ROLE_MANAGER")) return MembersRole.MANAGER;
            else if (str.equals("ROLE_ADMIN")) return MembersRole.ADMIN;
            else return MembersRole.USER;
          }
        }).collect(Collectors.toSet()))
        .build();
    return members;
  }

  default MembersDTO entityToDTO(Members members) {
    MembersDTO membersDTO = MembersDTO.builder()
        .mid(members.getMid())
        .email(members.getEmail())
        .password(members.getPassword())
        .nickname(members.getNickname())
        .name(members.getName())
        .fromSocial(members.isFromSocial())
        .regDate(members.getRegDate())
        .modDate(members.getModDate())
        .roleSet(members.getRoleSet().stream().map(new Function<MembersRole, String >() {
          @Override
          public String apply(MembersRole membersRole) {
            return new String("ROLE_" + membersRole.name());
          }
        }).collect(Collectors.toSet()))
        .build();
    return membersDTO;
  }
}
