package com.example.apiserver.service;

import com.example.apiserver.dto.*;
import com.example.apiserver.entity.Journal;
import com.example.apiserver.entity.Members;
import com.example.apiserver.entity.Photos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface JournalService {
  PageResultDTO<JournalDTO, Object[]> getList(PageRequestDTO pageRequestDTO);

  Long register(JournalDTO journalDTO);

  JournalDTO get(Long jno);

  void modify(JournalDTO journalDTO);

  List<String> removeWithCommentsAndPhotos(Long jno);

  void removePhotosbyUUID(String uuid);

  default Map<String, Object> dtoToEntity(JournalDTO journalDTO) {
    Map<String, Object> entityMap = new HashMap<>();

    Journal journal = Journal.builder()
        .jno(journalDTO.getJno())
        .title(journalDTO.getTitle())
        .content(journalDTO.getContent())
        .members(Members.builder().mid(journalDTO.getMembersDTO().getMid()).build())
        .build();
    entityMap.put("journal", journal);

    List<PhotosDTO> photosDTOList = journalDTO.getPhotosDTOList();
    if (photosDTOList != null && photosDTOList.size() > 0) {
      List<Photos> photosList = photosDTOList.stream().map(photosDTO -> {
        Photos photos = Photos.builder()
            .path(photosDTO.getPath())
            .photosName(photosDTO.getPhotosName())
            .uuid(photosDTO.getUuid())
            .journal(journal)
            .build();
        return photos;
      }).collect(Collectors.toList());
      entityMap.put("photosList", photosList);
    }
    return entityMap;
  }

  default JournalDTO entityToDTO(Journal journal, List<Photos> photosList,
                                 Members members,Long likes, Long commentsCnt) {

    MembersDTO membersDTO = MembersDTO.builder()
        .mid(members.getMid())
        .name(members.getName())
        .email(members.getEmail())
        .nickname(members.getNickname())
        .mobile(members.getMobile())
        .build();
    JournalDTO journalDTO = JournalDTO.builder()
        .jno(journal.getJno())
        .title(journal.getTitle())
        .content(journal.getContent())
        .membersDTO(membersDTO)
        .regDate(journal.getRegDate())
        .modDate(journal.getModDate())
        .build();
    List<PhotosDTO> photosDTOList = new ArrayList<>();
    if (photosList.size() > 0 && photosList.get(0) != null) {
      photosDTOList = photosList.stream().map(photos -> {
        PhotosDTO photosDTO = PhotosDTO.builder()
            .photosName(photos.getPhotosName())
            .path(photos.getPath())
            .uuid(photos.getUuid())
            .build();
        return photosDTO;
      }).collect(Collectors.toList());
    }
    journalDTO.setPhotosDTOList(photosDTOList);
    journalDTO.setLikes(likes);
    journalDTO.setCommentsCnt(commentsCnt);
    return journalDTO;
  }

}