package com.kh.demo.domain.product;

import com.kh.demo.domain.common.file.UploadFile;
import com.kh.demo.domain.common.file.UploadFileDAO;
import com.kh.demo.domain.common.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
//@Transactional
public class ProductSVCImpl implements ProductSVC{
  private final ProductDAO productDAO;
  private final UploadFileDAO uploadFileDAO;
  private final FileUtils fileUtils;

  //등록
  @Override
  public Long save(Product product) {
    return productDAO.save(product);
  }

  @Override
  public Long save(Product product, MultipartFile file) {
    //1)상품등록
    Long id = save(product);
    
    //2)첨부파일 메타정보 등록 (uploadfile테이블)
    UploadFile uploadFile = new UploadFile();
    uploadFile.setCode("P0101"); //상품관리
    uploadFile.setRid(id);
    uploadFile.setUploadFileName(file.getOriginalFilename());
    uploadFile.setStoreFileName(fileUtils.storeFileName(file.getOriginalFilename()));
    uploadFile.setFsize(String.valueOf(file.getSize()));
    uploadFile.setFtype(file.getContentType());
    uploadFileDAO.addFile(uploadFile);

    //3)첨부파일 저장소에 저장 (xxx-xx-xxx.png)
    try {
      String storeFileName = fileUtils.storeFileName(file.getOriginalFilename());
      file.transferTo(new File("d:/tmp/"+storeFileName));
    } catch (IOException e) {
      throw new RuntimeException("첨부파일 스토리지에 저장시 오류 발생!!");
    }

    return id;
  }

  @Override
  public Long save(Product product, List<MultipartFile> files) {
    //1)상품등록
    Long id = save(product);

    //2)첨부파일 메타정보 등록 (uploadfile테이블)
    List<UploadFile> uploadFiles = new ArrayList<>();

    for (MultipartFile file : files) {
      UploadFile uploadFile = new UploadFile();

      uploadFile.setCode("P0101"); //상품관리
      uploadFile.setRid(id);
      uploadFile.setUploadFileName(file.getOriginalFilename());
      uploadFile.setStoreFileName(fileUtils.storeFileName(file.getOriginalFilename()));
      uploadFile.setFsize(String.valueOf(file.getSize()));
      uploadFile.setFtype(file.getContentType());

      uploadFiles.add(uploadFile);
    }
    uploadFileDAO.addFile(uploadFiles);


    //3)첨부파일 저장소에 저장 (xxx-xx-xxx.png)
    try {
      for (MultipartFile file : files) {
        String storeFileName = fileUtils.storeFileName(file.getOriginalFilename());
        file.transferTo(new File("d:/tmp/" + storeFileName));
      }
    } catch (IOException e) {
      throw new RuntimeException("첨부파일 스토리지에 저장시 오류 발생!!");
    }

    return id;
  }

  @Override
  public Long save(Product product, MultipartFile file, List<MultipartFile> files) {
    return null;
  }



  //목록
  @Override
  public List<Product> findAll() {
    return productDAO.findAll();
  }

  //조회
  @Override
  public Optional<Product> findByProductId(Long productId) {
    return productDAO.findByProductId(productId);
  }

  //수정
  @Override
  public int update(Long productId, Product product) {
    return productDAO.update(productId,product);
  }

  //삭제
  @Override
  public int deleteByProductId(Long productId) {
    return productDAO.deleteByProductId(productId);
  }



}
