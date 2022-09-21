package com.kh.demo.domain.common.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileUtils {
  @Value("${attach.root_dir}") //application.properties 파일의 키값을 읽어옮
  private String attachRoot; //첨부파일 루트경로

  //MultipartFile -> UploadFile
  public UploadFile multipartFileToUpLoadFile(MultipartFile file,AttachCode code, Long rid){
    UploadFile uploadFile = new UploadFile();

    uploadFile.setCode(code.name());
    uploadFile.setRid(rid);
    uploadFile.setUploadFileName(file.getOriginalFilename());

    String storeFileName = storeFileName(file.getOriginalFilename());
    uploadFile.setStoreFileName(storeFileName);
    uploadFile.setFsize(String.valueOf(file.getSize()));
    uploadFile.setFtype(file.getContentType());

    //스토리지에 파일저장
    storageFile(file,code,storeFileName);
    return uploadFile;
  }

  //List<MultipartFile> -> List<UploadFile>
  public List<UploadFile> multipartFilesToUpLoadFiles(List<MultipartFile> files, AttachCode code, Long rid) {
    List<UploadFile> uploadFiles = new ArrayList<>();
    for (MultipartFile file : files) {
      UploadFile uploadFile = multipartFileToUpLoadFile(file, code, rid);
      uploadFiles.add(uploadFile);
      storageFile(file,code,uploadFile.getStoreFileName());
    }
    return uploadFiles;
  }

  //랜덤 파일 생성
  private String storeFileName(String originalFileName){
    //확장자 추출
    int dotPosition = originalFileName.indexOf(".");
    String ext = originalFileName.substring(dotPosition + 1);

    //랜덤파일명
    String storedFileName = UUID.randomUUID().toString();
    StringBuffer sb = new StringBuffer();
    storedFileName = sb.append(storedFileName)
        .append(".")
        .append(ext)
        .toString();
    return storedFileName;
  }

  //스토리지에 파일저장
  private void storageFile(MultipartFile file, AttachCode code, String storeFileName) {
    try {
      file.transferTo(new File(getPath(code, storeFileName)));
    } catch (IOException e) {
      throw new RuntimeException("첨부파일 스토리지 저장시 오류발생!");
    }
  }

  //첨부파일 경로
  private String getPath(AttachCode code, String storeFileName) {
    return this.attachRoot + code.name() + "/" + storeFileName;
  }
}
