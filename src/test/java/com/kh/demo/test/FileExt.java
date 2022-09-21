package com.kh.demo.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

@Slf4j
public class FileExt {

  @Test
  @DisplayName("확장자추출")
  void test() {
    
    //abc.jpg --> xxx-xxx-xxx.jpg
    String originalFileName = "abc.jpg";
    String storedFileName = UUID.randomUUID().toString();
    log.info("storedFileName={}", storedFileName);

    int dotPosition = originalFileName.indexOf(".");
    String ext = originalFileName.substring(dotPosition + 1);
    log.info("확장자={}",ext);

    //String fileName = storedFileName+"."+ext;
    StringBuffer fileName = new StringBuffer();
    String fName = fileName.append(storedFileName)
                           .append(".")
                           .append(ext)
                           .toString();
    log.info("파일명={}", fName);
  }

  @Test
  void test2(){
    String originalFileName = "test.png";
    String fileName = storeFileName(originalFileName);
    log.info("fileName={}", fileName);
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
}
