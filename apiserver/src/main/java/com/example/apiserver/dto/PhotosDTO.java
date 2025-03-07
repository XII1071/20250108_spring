package com.example.apiserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhotosDTO {
  private String uuid;
  private String photosName;
  private String path;
  public String getPhotosURL() {
    try {
      return URLEncoder.encode(path + "/" + uuid + "_" + photosName, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return "";
  }
  public String getThumbnailURL() {
    try {
      return URLEncoder.encode(path + "/s_" + uuid + "_" + photosName, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return "";
  }
}