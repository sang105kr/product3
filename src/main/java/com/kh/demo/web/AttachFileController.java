package com.kh.demo.web;

import com.kh.demo.domain.common.file.UploadFile;
import com.kh.demo.domain.common.file.UploadFileDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/attach")
public class AttachFileController {

  @Value("${attach.root_dir}")
  private String attachRoot; //첨부파일 루트경로
  private final UploadFileDAO uploadFileDAO;

  //이미지
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  @GetMapping("/img/{attachCode}/{storeFileName}")
  public Resource img(
      @PathVariable String attachCode,
      @PathVariable String storeFileName) throws MalformedURLException {
    // http://서버:포트/경로...
    // file:///d:/tmp/P0101/xxx-xxx-xxx-xxx.png
    Resource resource = new UrlResource("file:///"+attachRoot+"/"+attachCode+"/"+storeFileName);
    return resource;
  }

  //다운로드
  @GetMapping("/file/{attachCode}/{fid}")
  //반환 타입이 ResponseEntity<>인경우 응답메세지 바디에 직접쓰기작업시도함 (@ResponseBody불필요 )
  public ResponseEntity<Resource> file(
      @PathVariable String attachCode,
      @PathVariable Long fid  ) throws MalformedURLException {

    ResponseEntity<Resource> res = null;

    Optional<UploadFile> uploadFile = uploadFileDAO.findFileByUploadFileId(fid);
    if(uploadFile.isEmpty()) return res;

    UploadFile attachFile = uploadFile.get();
    log.info("attachFile={}", attachFile);
    String storeFileName = attachFile.getStoreFilename();
    String uploadFileName = attachFile.getUploadFilename();

    Resource resource = new UrlResource("file:///"+attachRoot+attachCode+"/"+storeFileName);

    //첨부파일명의 한글깨짐방지
    String encode = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
    //클라이언트가 파일을 다운로드해주기위해 응답메세지 헤더에 포함.
    String contentDisposition = "attachmemt; filename=\""+encode+"\"";

    res = ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,contentDisposition)
                        .body(resource);
    return res;
  }
}
