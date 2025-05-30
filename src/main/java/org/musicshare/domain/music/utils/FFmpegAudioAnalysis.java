package org.musicshare.domain.music.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

    private static final int MAX_FREQUENCY = 22050;
    private static final int MAX_ARRAY = 10;
    private static final int MAX_DURATION = 20;

    private FFmpegAudioAnalysis() {
        throw new UnsupportedOperationException("이 유틸리티 클래스는 인스턴스화할 수 없습니다.");
    }

    public static String analyzeAudio(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다");
        }

        // MultipartFile을 임시 파일로 저장
        String fileName = Objects.requireNonNull(file.getOriginalFilename());
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        File tempFile = File.createTempFile("uploaded_", extension);
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
        tempFile.delete();

        // 결과 반환
        return "Duration Data :" + musicDurationFormatMinSec + "\n" +
            "BPM Data :" + bpmResult + "\n" +
            //"RMS Data : " + rmsResult + "\n" +
            "Spectral Data : " + spectralResult;
    }

    public static double getMusicDuration(String filePath) throws Exception {
        // FFprobe 명령 실행
        String command = "ffprobe -i " + filePath + " -show_entries format=duration -v quiet -of csv=p=0";
        String durationStr = runCommand(command);

        if (durationStr.isEmpty()) {
            throw new Exception("재생 시간 분석 실패");
        }

        // durationStr이 숫자로 변환이 되지 않으면 예외 처리
        try {
            return Double.parseDouble(durationStr);
        } catch (NumberFormatException e) {
            throw new Exception("재생 시간 변환 실패: " + durationStr);
        }
    }

    // BPM 분석 (aubio 사용)
    private static String analyzeBPM(String filePath) throws Exception {
        String command = "aubio tempo -i " + filePath;
        return runCommand(command);
    }

    // RMS (음량) 분석
    private static String analyzeRMS(String filePath) throws Exception {
        String command = "ffmpeg -i " + filePath + " -af volumedetect -f null -";
        String output = runCommand(command);

        // RMS 데이터 파싱 (mean_volume 추출)
        Pattern pattern = Pattern.compile("mean_volume: ([\\-0-9\\.]+) dB");
        Matcher matcher = pattern.matcher(output);
        if (matcher.find()) {
            return "Mean Volume: " + matcher.group(1) + " dB";
        } else {
            throw new Exception("RMS 분석 실패");
        }
    }

    // 주파수 분석 (PNG 변환)
    private static String analyzeFrequency(String filePath, String mp3DurationFormatMinSec) throws Exception {
        File audioFile = new File(filePath);
        File pngFile = new File(audioFile.getParent(), "spectrum.png");

        // 음악 길이 (초 단위)
        int totalSeconds = convertToSeconds(mp3DurationFormatMinSec);
        // 음악 길이 제한
        if(totalSeconds > MAX_DURATION) totalSeconds = MAX_DURATION;

        // FFmpeg 실행 → spectrum.png 생성
        String command = "ffmpeg -i " + filePath + " -filter_complex [0:a]showfreqs=s=1920x1080 -t " + totalSeconds + " -update 1 " + pngFile.getAbsolutePath();

        runCommand(command);

        if (!pngFile.exists()) {
            throw new Exception("주파수 분석 실패 (이미지 파일 없음)");
        }

        // OpenCV로 spectrum.png 분석
        String frequeciesData = extractFrequenciesFromImage(pngFile.getAbsolutePath());

        // 임시 파일 삭제
        pngFile.delete();

        return frequeciesData;
    }

    // OpenCV로 spectrum.png 분석
    private static String extractFrequenciesFromImage(String imagePath) throws Exception {
        Mat image = opencv_imgcodecs.imread(imagePath, opencv_imgcodecs.IMREAD_GRAYSCALE);
        if (image.empty()) {
            throw new Exception("주파수 이미지 파일을 찾을 수 없음");
        }

        int height = image.rows();
        int width = image.cols();
        // 최대 주파수
        double frequencyPerPixel = (double) MAX_FREQUENCY / height;

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
        String[] firstLimitElements = Arrays.copyOfRange(elements, 0, MAX_ARRAY);

        // String으로 변환
        return String.join(", ", firstLimitElements);
    }

    // FFmpeg 또는 aubio 명령 실행
    private static String runCommand(String command) throws Exception {
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
                throw new Exception("명령어 실행 중 오류 발생, 종료 코드: " + exitCode);
            }
        } catch (Exception e) {
            throw new Exception("명령어 실행 중 오류 발생");
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
