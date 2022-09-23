package com.kh.demo.domain.common.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadFileSVCImpl implements UploadFileSVC{

  private final UploadFileDAO uploadFileDAO;
  private final FileUtils fileUtils;

  /**
   * 업로드 파일 등록 - 단건
   *
   * @param uploadFile
   * @return 파일Id
   */
  @Override
  public Long addFile(UploadFile uploadFile) {
    return uploadFileDAO.addFile(uploadFile);
  }

  /**
   * 업로드 파일 등록 - 여러건
   *
   * @param uploadFiles
   */
  @Override
  public void addFile(List<UploadFile> uploadFiles) {
    uploadFileDAO.addFile(uploadFiles);
  }

  /**
   * 업로드파일조회 단건
   *
   * @param code
   * @param rid
   * @return
   */
  @Override
  public List<UploadFile> getFilesByCodeWithRid(String code, Long rid) {
    return uploadFileDAO.getFilesByCodeWithRid(code,rid);
  }

  /**
   * 업로드파일조회 단건
   *
   * @param uploadfileId
   * @return
   */
  @Override
  public Optional<UploadFile> findFileByUploadFileId(Long uploadfileId) {
    return uploadFileDAO.findFileByUploadFileId(uploadfileId);
  }

  /**
   * 첨부파일 삭제 by uplaodfileId
   *
   * @param uploadfileId 첨부파일아이디
   * @return 삭제한 레코드수
   */
  @Override
  @Transactional
  public int deleteFileByUploadFildId(Long uploadfileId) {

    Optional<UploadFile> optional = uploadFileDAO.findFileByUploadFileId(uploadfileId);
    if(optional.isEmpty()) return 0;

    UploadFile uploadFile = optional.get();

    //1) 스토리지 파일을 삭제한다.
    fileUtils.deleteAttachFile(AttachCode.valueOf(uploadFile.getCode()),uploadFile.getStoreFilename());

    //2) 첨부파일의 메터정보를 삭제한다.
    int affectedRow = uploadFileDAO.deleteFileByUploadFildId(uploadfileId);

    return affectedRow;
  }

  /**
   * 첨부파일 삭제 By code, rid
   *
   * @param code 첨부파일 분류코드
   * @param rid  첨부파일아이디
   * @return 삭제한 레코드수
   */
  @Override
  public int deleteFileByCodeWithRid(String code, Long rid) {
    return uploadFileDAO.deleteFileByCodeWithRid(code, rid);
  }
}
