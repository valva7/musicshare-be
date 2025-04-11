package org.musicshare.common.file;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface S3Uploader {

    /**
     * 파일 업로드
     *
     * @param multipartFile 업로드할 파일
     * @return 업로드된 파일의 URL
     * @throws IOException 파일 변환 중 오류 발생 시
     */
    String fileUpload(MultipartFile multipartFile) throws IOException;

    /**
     * 파일 삭제
     *
     * @param fileName 삭제할 파일의 URL
     */
    void fileDelete(String fileName);

}
