package com.example.ex6.dto;

import lombok.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MovieImageDTO {
  private String uuid;
  private String imgName;
  private String path;
  // ✅ 삭제 여부 확인 메서드 추가
  private boolean deleted; // ✅ 삭제 여부 추가

  public String getImageURL() {
    try {
      return URLEncoder.encode(path + "/" + uuid + "_" + imgName, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return "";
  }
  public String getThumbnailURL() {
    try {
      return URLEncoder.encode(path + "/s_" + uuid + "_" + imgName, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return "";
  }
}