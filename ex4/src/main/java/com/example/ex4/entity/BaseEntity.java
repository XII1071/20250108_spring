package com.example.ex4.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/*
 *엔티티 등록된 시간이 있을것이고 수정하는것이 있고 붙어야지만 속성
 *모든 클래스가 상속받도록 해주는것 레지데이터와 모디데이터에서
 *
 * */

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
abstract class BaseEntity {

  @CreatedDate
  @Column(name = "regdate", updatable = false)
  private LocalDateTime regDate; /*등록*/

  @LastModifiedDate
  @Column(name = "moddate")
  private LocalDateTime modDate;  /*수정*/
}
