package com.kh.demo.web;

import com.kh.demo.domain.common.file.UploadFile;
import com.kh.demo.domain.common.file.UploadFileDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
  //@ResponseStatus(HttpStatus.OK)
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

    Resource resource = new UrlResource("file:"+attachRoot+attachCode+"/"+storeFileName);
    String encode = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);

    String contentDisposition = "attachmemt; filename=\""+encode+"\"";

    res = ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION,contentDisposition)
                        .body(resource);
    return res;
  }
}
