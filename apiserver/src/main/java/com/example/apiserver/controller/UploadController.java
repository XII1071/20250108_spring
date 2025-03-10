package com.example.apiserver.controller;

import com.example.apiserver.dto.UploadResultDTO;
import com.example.apiserver.service.JournalService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
@RequiredArgsConstructor
public class UploadController {

  private final JournalService journalService;

  @Value("${com.example.upload.path}")
  private String uploadPath;

  @PostMapping("/uploadAjax")
  public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles) {
    List<UploadResultDTO> resultDTOList = new ArrayList<>();

    for (MultipartFile uploadFile : uploadFiles) {
      // 이미지 파일만 업로드
      if (uploadFile.getContentType().startsWith("image") == false) {
        log.warn("this file is not image type");
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
      }

      String originalName = uploadFile.getOriginalFilename(); // 파일의 전체 경로

      // 실제 파일명만 추출
      String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

      // 저장될 경로 생성
      String folderPath = makeFolder();
      log.info("folderPath: " + folderPath);

      String uuid = UUID.randomUUID().toString();
      String saveName = uploadPath + File.separator + folderPath
          + File.separator + uuid + "_" + fileName;
      Path savePath = Paths.get(saveName);// file명을 제외한 경로만 저장

      try {
        uploadFile.transferTo(savePath);

        //thumbnail 생성
        String thumbnailSaveName = uploadPath + File.separator + folderPath
            + File.separator + "s_" + uuid + "_" + fileName;
        File thumbnailFile = new File(thumbnailSaveName);
        Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);

        resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
  }

  @GetMapping("/display")
  public ResponseEntity<byte[]> getImageFile(String fileName, String size) {
    ResponseEntity<byte[]> result = null;
    try {
      String searchFilename = URLDecoder.decode(fileName, "UTF-8");
      File file = new File(uploadPath + File.separator + searchFilename);
      if (size != null && size.equals("1")) {
        log.info(">>", file.getName());
        // 미리보기 할 때 링크에 size=1로 설정하여 섬네일명에서 s_ 를 제거하고 가져옴
        file = new File(file.getParent(), file.getName().substring(2));
      }
      log.info("file: " + file);
      HttpHeaders headers = new HttpHeaders();
      // 파일의 확장자에 따라서 브라우저에 전송하는 MIME타입을 결정
      headers.add("Content-Type", Files.probeContentType(file.toPath()));
      result = new ResponseEntity<>(
          FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);
    } catch (Exception e) {
      log.error(e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return result;
  }

  @Transactional
  @PostMapping("/removeFile")
  public ResponseEntity<Boolean> removeFile(String fileName, String uuid) {
    log.info("<<", fileName);
    ResponseEntity<Boolean> result = null;
    String srchFileName = null;

    if (uuid != null) {
      journalService.removePhotosbyUUID(uuid);
    }

    try {
      srchFileName = URLDecoder.decode(fileName, "UTF-8");
      File file = new File(uploadPath + File.separator + srchFileName);
      boolean tmpRemove1 = file.delete();
      File thumbnail = new File(file.getParent(), "s_" + file.getName());
      boolean tmpRemove2 = thumbnail.delete();
      boolean lastRemove = tmpRemove1 && tmpRemove2;
      result = new ResponseEntity<>(lastRemove,
          lastRemove ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    } catch (UnsupportedEncodingException e) {
      log.error(e.getMessage());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return result;
  }

  private String makeFolder() {
    String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    String folderPath = str.replace("/", File.separator);
    File uploadPathFolder = new File(uploadPath, folderPath);
    if (!uploadPathFolder.exists()) uploadPathFolder.mkdirs();
    return folderPath;
  }
}
