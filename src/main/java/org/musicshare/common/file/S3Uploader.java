package org.musicshare.common.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.musicshare.global.exception.S3FileProcessException;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public abstract class S3Uploader {

    protected final String bucket;
    protected final Set<String> fileExtensions;
    protected final String directory;
    protected final AmazonS3 amazonS3;

    protected S3Uploader(String bucket, Set<String> fileExtensions, String directory, AmazonS3 amazonS3) {
        this.bucket = bucket;
        this.fileExtensions = fileExtensions;
        this.directory = directory;
        this.amazonS3 = amazonS3;
    }

    /**
     * 파일 업로드
     *
     * @param multipartFile 업로드할 파일
     * @return 업로드된 파일의 URL
     * @throws IOException 파일 변환 중 오류 발생 시
     */
    public abstract String fileUpload(MultipartFile multipartFile) throws IOException;

    /**
     * 파일 삭제
     *
     * @param fileName 삭제할 파일의 URL
     */
    public void fileDelete(String fileName) {
        // URL에서 Directory만 추출
        String[] split = fileName.split(".com");
        // URL 디코딩을 통해 원래의 파일 이름을 가져옴
        String decodedFileName = URLDecoder.decode(split[1], StandardCharsets.UTF_8);
        log.info("Deleting file from S3: {}", decodedFileName);
        amazonS3.deleteObject(bucket, decodedFileName);
    }

    // ========================================= Inner Method =========================================
    protected File convert(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = createUniqueFileName(Objects.requireNonNull(originalFileName));

        File convertFile = new File(uniqueFileName);
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            } catch (IOException e) {
                log.error("파일 변환 중 오류 발생: {}", e.getMessage());
                throw new S3FileProcessException("파일 변환 중 오류 발생: " + e.getMessage());
            }
        }
        return convertFile;
    }

    protected String putS3(File uploadFile, String fileName) {
        if (!uploadFile.exists()) {
            throw new S3FileProcessException("파일이 비어있음");
        }

        String uploadDirectory = directory + fileName;
        amazonS3.putObject(new PutObjectRequest(bucket, uploadDirectory, uploadFile));
        String uploadUrl = amazonS3.getUrl(bucket, uploadDirectory).toString();
        log.info("S3 Upload URL: {}", uploadUrl);
        return uploadUrl;
    }

    protected void removeNewFile(File targetFile) {
        if (!targetFile.delete()) {
            throw new S3FileProcessException("파일 삭제 실패: " + targetFile.getName());
        }
    }

    protected String createUniqueFileName(String originalFileName) {
        String uuid = UUID.randomUUID().toString();
        return directory + uuid + "_" + originalFileName.replaceAll("\\s", "_");
    }

    protected String fileValidate(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            log.error("파일이 비어있음");
            throw new S3FileProcessException("파일이 비어있음");
        }

        // 파일 이름에서 공백을 제거한 새로운 파일 이름 생성
        String originalFileName = multipartFile.getOriginalFilename();

        if (originalFileName == null || originalFileName.isBlank()) {
            log.error("파일 이름이 비어있음");
            throw new S3FileProcessException("파일 이름이 비어있음");
        }

        // 파일 확장자 확인
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        if (fileExtensions.contains(fileExtension.toLowerCase())) {
            log.error("지원하지 않는 파일 형식: {}", fileExtension);
            throw new S3FileProcessException("지원하지 않는 파일 형식: " + fileExtension);
        }

        return originalFileName;
    }

}
