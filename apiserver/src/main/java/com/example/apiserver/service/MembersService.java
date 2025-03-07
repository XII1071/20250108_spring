package com.example.apiserver.service;

import com.example.apiserver.dto.MembersDTO;

public interface MembersService {
  Long registerMembers(MembersDTO membersDTO);
  Long updateMembers(MembersDTO membersDTO);
  void removeMembers(Long mid);
  MembersDTO getMembers(Long mid);
  MembersDTO loginCheck(String email);
}
