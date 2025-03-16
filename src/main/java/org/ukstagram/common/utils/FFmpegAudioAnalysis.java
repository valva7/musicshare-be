package org.ukstagram.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class FFmpegAudioAnalysis {

    private final static String[] FFPROBE_PROCESS_COMMAND = {
        "ffprobe", "-i", "-show_entries", "format=duration", "-v", "quiet", "-of", "csv=p=0"
    };

    private final static int ARRAY_LIMIT = 10;

    public static String analyzeAudio(MultipartFile file) throws Exception {
        // MultipartFile을 임시 파일로 저장
        File tempFile = File.createTempFile("uploaded_", ".mp3");
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        if (!tempFile.exists()) {
            throw new IllegalArgumentException("파일이 존재하지 않습니다: " + tempFile.getAbsolutePath());
        }

        String filePath = tempFile.getAbsolutePath();

        // FFprobe 실행하여 음악 재생 길이 가져오기
        double duration = getMusicDuration(filePath);
        String musicDurationFormatMinSec = formatDuration(duration);

        // BPM 분석 (aubio 사용)
        String bpmResult = analyzeBPM(filePath);

        // RMS(음량) 분석 (수정 필요)
        // String rmsResult = analyzeRMS(filePath);

        // 주파수 분석
        String spectralResult = analyzeFrequency(filePath, musicDurationFormatMinSec);

        // 임시 파일 삭제
        //tempFile.delete();

        // 결과 반환
        return "Duration Data :" + musicDurationFormatMinSec + "\n" +
            "BPM Data :" + bpmResult + "\n" +
            //"RMS Data : " + rmsResult + "\n" +
            "Spectral Data : " + spectralResult;
    }

    public static double getMusicDuration(String filePath) throws Exception {
        // FFprobe 명령 실행
        ProcessBuilder processBuilder = new ProcessBuilder(
            FFPROBE_PROCESS_COMMAND[0],
            FFPROBE_PROCESS_COMMAND[1],
            filePath,
            FFPROBE_PROCESS_COMMAND[2],
            FFPROBE_PROCESS_COMMAND[3],
            FFPROBE_PROCESS_COMMAND[4],
            FFPROBE_PROCESS_COMMAND[5],
            FFPROBE_PROCESS_COMMAND[6],
            FFPROBE_PROCESS_COMMAND[7]
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

    // BPM 분석 (aubio 사용)
    private static String analyzeBPM(String filePath) {
        String command = "aubio tempo -i " + filePath;
        return runCommand(command);
    }

    // RMS (음량) 분석
    private static String analyzeRMS(String filePath) {
        String command = "ffmpeg -i " + filePath + " -af volumedetect -f null -";
        String output = runCommand(command);

        // RMS 데이터 파싱 (mean_volume 추출)
        Pattern pattern = Pattern.compile("mean_volume: ([\\-0-9\\.]+) dB");
        Matcher matcher = pattern.matcher(output);
        if (matcher.find()) {
            return "Mean Volume: " + matcher.group(1) + " dB";
        } else {
            return "RMS 분석 실패 (데이터 없음)";
        }
    }

    // 주파수 분석 (PNG 변환)
    private static String analyzeFrequency(String filePath, String mp3DurationFormatMinSec) {
        File audioFile = new File(filePath);
        File pngFile = new File(audioFile.getParent(), "spectrum.png");

        // 음악 길이 (초 단위)
        int totalSeconds = convertToSeconds(mp3DurationFormatMinSec);
        // 음악 길이 제한
        if(totalSeconds > 20) totalSeconds = 20;

        // FFmpeg 실행 → spectrum.png 생성
        String command = "ffmpeg -i " + filePath + " -filter_complex [0:a]showfreqs=s=1920x1080 -t " + totalSeconds + " -update 1 " + pngFile.getAbsolutePath();

        runCommand(command);

        if (!pngFile.exists()) {
            return "주파수 분석 실패 (spectrum.png 생성 안됨)";
        }

        // OpenCV로 spectrum.png 분석
        String frequeciesData = extractFrequenciesFromImage(pngFile.getAbsolutePath());

        // 임시 파일 삭제
        pngFile.delete();

        return frequeciesData;
    }

    // OpenCV로 spectrum.png 분석
    private static String extractFrequenciesFromImage(String imagePath) {
        Mat image = opencv_imgcodecs.imread(imagePath, opencv_imgcodecs.IMREAD_GRAYSCALE);
        if (image.empty()) {
            return "이미지를 로드할 수 없습니다.";
        }

        int height = image.rows();
        int width = image.cols();
        int maxFrequency = 22050;  // 최대 주파수
        double frequencyPerPixel = (double) maxFrequency / height;

        List<Double> strongestFreqs = new ArrayList<>();

        for (int x = 0; x < width; x++) {
            double maxIntensity = 0;
            int maxIndex = 0;

            for (int y = 0; y < height; y++) {
                double intensity = image.ptr(y, x).get();
                if (intensity > maxIntensity) {
                    maxIntensity = intensity;
                    maxIndex = y;
                }
            }

            double frequency = maxIndex * frequencyPerPixel;
            strongestFreqs.add(frequency);
        }

        // 주파수 데이터를 String으로 변환 후 ARRAY_LIMIT 갯수만큼 사용
        String arr = strongestFreqs.toString();
        arr = arr.substring(1, arr.length() - 1);
        String[] elements = arr.split(", ");
        // 200번째 배열까지만 자르기
        String[] firstLimitElements = Arrays.copyOfRange(elements, 0, ARRAY_LIMIT);
        // String으로 변환
        String convertStr = String.join(", ", firstLimitElements);

        return convertStr;
    }

    // FFmpeg 또는 aubio 명령 실행
    private static String runCommand(String command) {
        StringBuilder output = new StringBuilder();
        try {
            // ProcessBuilder를 사용해 명령어를 실행
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.redirectErrorStream(true); // 에러 출력도 표준 출력으로 합침
            Process process = processBuilder.start();

            // 명령어 실행 후 출력을 읽어들이는 부분
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 프로세스 종료 대기
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                return "명령어 실행 중 오류 발생, 종료 코드: " + exitCode;
            }
        } catch (Exception e) {
            return "오류 발생: " + e.getMessage();
        }
        return output.toString();
    }

    private static String formatDuration(double seconds) {
        long minutes = TimeUnit.SECONDS.toMinutes((long) seconds);
        long remainingSeconds = (long) seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    public static int convertToSeconds(String duration) {
        // "mm:ss" 형식으로 들어오는 문자열을 ':' 기준으로 분리
        String[] parts = duration.split(":");
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);

        // 분을 초로 변환하고 초를 더하여 총 초를 계산
        return minutes * 60 + seconds;
    }
}
