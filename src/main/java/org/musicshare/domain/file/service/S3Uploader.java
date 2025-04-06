package org.musicshare.domain.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class S3Uploader {

    private static final String MUSIC_UPLOAD_DIR = "music/";

    private final AmazonS3 amazonS3;
    private final String bucket;

    public S3Uploader(AmazonS3 amazonS3, @Value("${cloud.aws.s3.bucket}") String bucket) {
        this.amazonS3 = amazonS3;
        this.bucket = bucket;
    }

    /**
     * 음악 파일 업로드
     *
     * @param multipartFile 업로드할 파일
     * @return 업로드된 파일의 URL
     * @throws IOException 파일 변환 중 오류 발생 시
     */
    public String musicFileUpload(MultipartFile multipartFile) throws IOException {
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

    /**
     * 음악 파일 삭제
     *
     * @param fileName 삭제할 파일의 URL
     */
    public void deleteFile(String fileName) {
        // URL에서 Directory만 추출
        String[] split = fileName.split(".com");
        // URL 디코딩을 통해 원래의 파일 이름을 가져옴
        String decodedFileName = URLDecoder.decode(split[1], StandardCharsets.UTF_8);
        log.info("Deleting file from S3: {}", decodedFileName);
        amazonS3.deleteObject(bucket, decodedFileName);
    }

    private File convert(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = createUniqueFileName(Objects.requireNonNull(originalFileName));

        File convertFile = new File(uniqueFileName);
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            } catch (IOException e) {
                log.error("파일 변환 중 오류 발생: {}", e.getMessage());
                throw e;
            }
            return convertFile;
        }
        throw new IllegalArgumentException(String.format("파일 변환에 실패했습니다. %s", originalFileName));
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile));
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private static String createUniqueFileName(String originalFileName) {
        String uuid = UUID.randomUUID().toString();
        return uuid + "_" + originalFileName.replaceAll("\\s", "_");
    }

}