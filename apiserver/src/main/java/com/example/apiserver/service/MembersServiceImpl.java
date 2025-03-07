package com.example.apiserver.service;


import com.example.apiserver.dto.MembersDTO;
import com.example.apiserver.entity.Members;
import com.example.apiserver.repository.MembersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MembersServiceImpl implements MembersService {
  private final MembersRepository membersRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public MembersDTO getMembers(Long mid) {
    Optional<Members> result = membersRepository.findById(mid);
    if (result.isPresent()) return entityToDTO(result.get());
    return null;
  }

  @Override
  public MembersDTO loginCheck(String email) {
    Optional<Members> result = membersRepository.findByEmail(email);
    if (result.isPresent()) return entityToDTO(result.get());
    return null;
  }

  @Override
  public void removeMembers(Long mid) {
    membersRepository.deleteById(mid); // 가급적 사용하지 말라.
  }

  @Override
  public Long updateMembers(MembersDTO membersDTO) {
    Optional<Members> result = membersRepository.findById(membersDTO.getMid());
    if (result.isPresent()) {
      Members members = result.get();
      /* 변경할 내용은 members에 membersDTO의 내용을 변경하시오 */
      return membersRepository.save(members).getMid();
    }
    return 0L;
  }

  @Override
  public Long registerMembers(MembersDTO membersDTO) {
    membersDTO.setPassword(passwordEncoder.encode(membersDTO.getPassword()));
    return membersRepository.save(dtoToEnitity(membersDTO)).getMid();
  }

}
