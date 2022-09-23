package com.kh.demo.domain.product;

import com.kh.demo.domain.common.file.AttachCode;
import com.kh.demo.domain.common.file.FileUtils;
import com.kh.demo.domain.common.file.UploadFile;
import com.kh.demo.domain.common.file.UploadFileSVC;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductSVCImpl implements ProductSVC{
  private final ProductDAO productDAO;
  private final UploadFileSVC uploadFileSVC;
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
    //2)첨부파일-상품설명
    uploadFileSVC.addFile(file,AttachCode.P0101,id);
    return id;
  }

  @Override
  public Long save(Product product, List<MultipartFile> files) {
    //1)상품등록
    Long id = save(product);
    //2)첨부파일-이미지
    uploadFileSVC.addFile(files,AttachCode.P0102,id);
    return id;
  }

  @Override
  public Long save(Product product, MultipartFile file, List<MultipartFile> files) {
    //1)상품등록
    Long id = save(product);
    //2)첨부파일-상품설명
    uploadFileSVC.addFile(file,AttachCode.P0101,id);
    //2)첨부파일-이미지
    uploadFileSVC.addFile(files,AttachCode.P0102,id);
    return id;
  }

  //목록
  @Transactional(readOnly = true)
  @Override
  public List<Product> findAll() {
    return productDAO.findAll();
  }

  //조회
  @Transactional(readOnly = true)
  @Override
  public Optional<Product> findByProductId(Long productId) {
    return productDAO.findByProductId(productId);
  }

  //수정
  @Override
  public int update(Long productId, Product product) {
    return productDAO.update(productId,product);
  }

  @Override
  public int update(Long productId, Product product, MultipartFile file) {
    //1) 상품변경
    int affectedRow = update(productId, product);
    //2)첨부파일-상품설명
    uploadFileSVC.addFile(file,AttachCode.P0101,productId);
    return affectedRow;
  }

  @Override
  public int update(Long productId, Product product, MultipartFile file, List<MultipartFile> files) {
    //1) 상품변경
    int affectedRow = update(productId, product);
    //2)첨부파일-상품설명
    uploadFileSVC.addFile(file,AttachCode.P0101,productId);
    //3)첨부파일-이미지
    uploadFileSVC.addFile(files,AttachCode.P0102,productId);
    return affectedRow;
  }

  @Override
  public int update(Long productId, Product product, List<MultipartFile> files) {
    //1) 상품변경
    int affectedRow = update(productId, product);
    //2)첨부파일-이미지
    uploadFileSVC.addFile(files,AttachCode.P0102,productId);
    return affectedRow;
  }

  //삭제
  @Override
  public int deleteByProductId(Long productId) {

    //1) 첨부파일 메타정보 조회
    List<UploadFile> attachFiles = uploadFileSVC.getFilesByCodeWithRid(AttachCode.P0101.name(), productId);
    List<UploadFile> imageFiles = uploadFileSVC.getFilesByCodeWithRid(AttachCode.P0102.name(), productId);

    //2) 스토리지 파일 삭제
    //case1)
    attachFiles.stream().forEach(file->fileUtils.deleteAttachFile(AttachCode.valueOf(file.getCode()),file.getStoreFilename()));
    imageFiles.stream().forEach(file->fileUtils.deleteAttachFile(AttachCode.valueOf(file.getCode()),file.getStoreFilename()));

    //case2)
//    List<UploadFile> unionFiles = new ArrayList<>();
//    unionFiles.addAll(attachFiles);
//    unionFiles.addAll(imageFiles);
//    for(UploadFile file: unionFiles){
//      fileUtils.deleteAttachFile(AttachCode.valueOf(file.getCode()),file.getStoreFilename());
//    }

    //case3)
//    List<UploadFile> unionFiles = new ArrayList<>();
//    unionFiles.addAll(attachFiles);
//    unionFiles.addAll(imageFiles);
//    unionFiles.stream().forEach(file->fileUtils.deleteAttachFile(AttachCode.valueOf(file.getCode()),file.getStoreFilename()));

    //3) 상품정보 삭제
    int affectedRow = productDAO.deleteByProductId(productId);
    
    //4) 상품 메타정보 삭제
    uploadFileSVC.deleteFileByCodeWithRid(AttachCode.P0101.name(), productId);
    uploadFileSVC.deleteFileByCodeWithRid(AttachCode.P0102.name(), productId);

    return affectedRow;
  }



}
