package com.example.apiserver.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "journal")
public class Photos extends BaseEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long pno;

  private String uuid;
  private String photosName;
  private String path;

  @ManyToOne(fetch = FetchType.LAZY)
  private Journal journal;
}