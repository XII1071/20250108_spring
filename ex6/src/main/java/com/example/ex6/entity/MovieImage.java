package com.example.ex6.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "movie")
public class MovieImage extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long inum;

  private String uuid;

  private String imgName;

  private String path;

  @ManyToOne(fetch = FetchType.LAZY)
  private Movie movie;
}
