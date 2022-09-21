package com.kh.demo.domain.common.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FileUtils {
  //랜덤 파일 생성
  public String storeFileName(String originalFileName){
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
}
