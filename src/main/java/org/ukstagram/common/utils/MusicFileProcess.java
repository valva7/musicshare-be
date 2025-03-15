package org.ukstagram.common.utils;

import java.util.concurrent.TimeUnit;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class MusicFileProcess {

    private final static String[] FFPROBE_PROCESS_COMMAND = {
        "ffprobe", "-i", "", "-show_entries", "format=duration", "-v", "quiet", "-of", "csv=p=0"
    };

    public static String getMp3Duration(MultipartFile file) throws Exception {
        // 1. MultipartFile을 임시 파일로 저장
        File tempFile = File.createTempFile("uploaded_", ".mp3");
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        // 2. FFprobe 실행하여 MP3 길이 가져오기
        double duration = getMp3Duration(tempFile);

        // 3. 임시 파일 삭제
        tempFile.delete();

        return formatDuration(duration);
    }

    public static double getMp3Duration(File file) throws Exception {
        if (!file.exists()) {
            throw new IllegalArgumentException("파일이 존재하지 않습니다: " + file.getAbsolutePath());
        }

        // FFprobe 명령 실행
        ProcessBuilder processBuilder = new ProcessBuilder(
            "ffprobe", "-i", file.getAbsolutePath(), "-show_entries", "stream=duration", "-v", "quiet", "-of", "csv=p=0"
        );
        processBuilder.redirectErrorStream(true); // 오류 스트림을 표준 출력과 합침
        Process process = processBuilder.start();

        // FFprobe 실행 결과 읽기
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String durationStr = reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("FFprobe Output Line: " + line);
        }

        // FFprobe 오류 출력 확인
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String errorMessage = null;
        while ((errorMessage = errorReader.readLine()) != null) {
            System.out.println("FFprobe Error: " + errorMessage);
        }

        process.waitFor();  // 프로세스 종료 대기

        if (durationStr == null || durationStr.isEmpty()) {
            throw new Exception("FFprobe failed to retrieve duration. Please check the error messages above.");
        }

        // durationStr을 출력해서 확인
        System.out.println("FFprobe Output: " + durationStr);

        // durationStr이 숫자로 변환이 되지 않으면 예외 처리
        try {
            return Double.parseDouble(durationStr);
        } catch (NumberFormatException e) {
            throw new Exception("Failed to parse duration: " + durationStr);
        }
    }




    private static String formatDuration(double seconds) {
        long minutes = TimeUnit.SECONDS.toMinutes((long) seconds);
        long remainingSeconds = (long) seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }
}
