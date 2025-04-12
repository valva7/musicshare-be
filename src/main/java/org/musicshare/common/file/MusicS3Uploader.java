package org.musicshare.common.file;

import com.amazonaws.services.s3.AmazonS3;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component("Music3SUploader")
public class MusicS3Uploader extends S3Uploader {

    private static final Set<String> FILE_EXTENSIONS = Set.of("mp3", "wav");

    public MusicS3Uploader(@Value("${cloud.aws.s3.bucket}") String bucket, AmazonS3 amazonS3) {
        super(bucket, FILE_EXTENSIONS, S3Directory.MUSIC.getDirectory(), amazonS3);
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
        String originalFileName = fileValidate(multipartFile);

        // UUID를 파일명에 추가
        String uniqueFileName = createUniqueFileName(Objects.requireNonNull(originalFileName));

        // MultipartFile을 File로 변환
        File uploadFile = convert(multipartFile);

        // S3에 파일 업로드
        String uploadFileUrl = putS3(uploadFile, uniqueFileName);

        // 업로드 후 로컬 파일 삭제
        removeNewFile(uploadFile);

        return uploadFileUrl;
    }


}
