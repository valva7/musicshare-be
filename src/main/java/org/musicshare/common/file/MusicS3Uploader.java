package org.musicshare.common.file;

import com.amazonaws.services.s3.AmazonS3;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component("Music3SUploader")
public class MusicS3Uploader extends S3Uploader {

    private static final String MUSIC_UPLOAD_DIR = FileDirectory.MUSIC.getDirectory();

    public MusicS3Uploader(@Value("${cloud.aws.s3.bucket}") String bucket, AmazonS3 amazonS3) {
        super(bucket, FileDirectory.MUSIC.getDirectory(), amazonS3);
    }

    /**
     * 음악 파일 업로드
     *
     * @param multipartFile 업로드할 파일
     * @return 업로드된 파일의 URL
     * @throws IOException 파일 변환 중 오류 발생 시
     */
    @Override
    public String fileUpload(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다");
        }

        // 파일 이름에서 공백을 제거한 새로운 파일 이름 생성
        String originalFileName = multipartFile.getOriginalFilename();

        // UUID를 파일명에 추가
        String uniqueFileName = createUniqueFileName(Objects.requireNonNull(originalFileName));

        String fileName = MUSIC_UPLOAD_DIR + uniqueFileName;
        File uploadFile = convert(multipartFile);

        String uploadFileUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadFileUrl;
    }

}
