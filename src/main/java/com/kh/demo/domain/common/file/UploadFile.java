package com.kh.demo.domain.common.file;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UploadFile {
  private Long uploadFileId;          //  UPLOADFILE_ID	NUMBER(10,0)
  private String code;                //  CODE	VARCHAR2(11 BYTE)
  private Long rid;                   //  RID	VARCHAR2(10 BYTE)
  private String storeFileName;       //  STORE_FILENAME	VARCHAR2(100 BYTE)
  private String uploadFileName;      //  UPLOAD_FILENAME	VARCHAR2(100 BYTE)
  private String fsize;               //  FSIZE	VARCHAR2(45 BYTE)
  private String ftype;                //  FTYPE	VARCHAR2(100 BYTE)
  private LocalDateTime cdate;        //  CDATE	TIMESTAMP(6)
  private LocalDateTime udate;        //  UDATE	TIMESTAMP(6)
}
