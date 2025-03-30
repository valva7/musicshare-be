package org.musicshare.domain.music.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.musicshare.domain.music.dto.res.MusicDetailRes;
import org.musicshare.domain.music.repository.MusicFileRepository;
import org.musicshare.domain.music.repository.MusicRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.musicshare.common.utils.FFmpegAudioAnalysis;
import org.musicshare.domain.ai.dto.res.GptRes;
import org.musicshare.domain.ai.service.OpenAiService;
import org.musicshare.domain.file.service.S3Uploader;
import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.music.dto.res.PopularMusicRes;
import org.musicshare.domain.music.model.Music;
import org.musicshare.domain.music.model.MusicFile;
import org.musicshare.domain.music.model.MusicFileInfo;
import org.musicshare.domain.music.model.MusicInfo;
import org.musicshare.domain.music.model.entity.MusicEntity;
import org.musicshare.domain.music.model.entity.MusicFileEntity;
import org.musicshare.domain.music.repository.JpaMusicFileRepository;
import org.musicshare.domain.music.repository.JpaMusicRepository;
import org.musicshare.domain.member.repository.MemberRepository;
import org.musicshare.global.pricipal.UserAuth;

@Slf4j
@Service
@AllArgsConstructor
public class MusicService {

    private final OpenAiService openAiService;
    private final S3Uploader s3Uploader;

    private final JpaMusicRepository jpaMusicRepository;
    private final MusicRepository musicRepository;
    private final JpaMusicFileRepository jpaMusicFileRepository;
    private final MusicFileRepository musicFileRepository;
    private final MemberRepository memberRepository;

    private final EntityManager entityManager;


    @Transactional
    public void uploadMusicFile(UserAuth userAuth, MultipartFile file, String title, String description, String genre, String theme, String tags) throws Exception {
        String uploadUrl = "";
        try {
            Member member = memberRepository.findMemberById(userAuth.getUserId());
            // 음악 분석 및 음악정보 생성
            MusicInfo musicInfo = analyzeMusic(file, title, description, genre, theme, tags);
            Music music = musicRepository.saveMusic(musicInfo, member);

            // 음악 파일 S3 업로드
            uploadUrl = uploadMusicFileToS3(file);

            musicFileRepository.saveMusicFile(file.getOriginalFilename(), music, uploadUrl);
        } catch (DataIntegrityViolationException | PersistenceException e) {
            // JPA 예외 발생 시 S3에 업로드한 파일 삭제
            handleException(uploadUrl);
        }
    }

    public List<PopularMusicRes> getTop10ByCurrentMonthOrWeekOrderByLikes(String genre) {
        return musicRepository.findTop10ByCurrentMonthOrWeekOrderByLikes(genre);
    }

    public MusicDetailRes getMusicDetail(Long musicId){
        return jpaMusicRepository.findMusicById(musicId);
    }

    // ============================================================ Inner Method ============================================================
    private MusicInfo analyzeMusic(MultipartFile file, String title, String description, String genre, String theme, String tags) throws Exception {
        // FFMPEG으로 음악 특징 분석
        String musicAnalysisData = FFmpegAudioAnalysis.analyzeAudio(file);

        // AI에게 음악 분위기 분석 요청
        GptRes gptRes = openAiService.analyzeMusicWithGPT(musicAnalysisData);

        // 음악 분석 정보
        String duration = extractDuration(musicAnalysisData);
        int bpm = extractBPM(musicAnalysisData);
        String mood = gptRes.getChoices().get(0).getMessage().getContent();

        return new MusicInfo(title, theme, mood, genre, tags, bpm, duration, description, 0, 0, 0, 0);
    }

    private String uploadMusicFileToS3(MultipartFile file) throws IOException {
        try {
            // 음악 파일 S3 업로드
            return s3Uploader.musicFileUpload(file);
        } catch (IOException e) {
            throw new IOException("파일 업로드 실패", e);
        }
    }

    private void handleException(String uploadUrl) {
        // S3에 업로드한 파일 삭제
        if (uploadUrl != null && !uploadUrl.isEmpty()) {
            s3Uploader.deleteFile(uploadUrl);
        }
        log.error("데이터 저장 중 오류 발생. S3에서 파일 삭제: {}", uploadUrl);
    }

    // test 문자열에서 Duration Data 추출
    private static String extractDuration(String test) {
        String durationPrefix = "Duration Data :";
        int startIdx = test.indexOf(durationPrefix);

        if (startIdx == -1) {
            return ""; // Duration 데이터가 없는 경우 처리
        }

        // Duration 데이터 시작 위치를 찾아서 그 뒤에 있는 값을 추출
        startIdx += durationPrefix.length();
        int endIdx = test.indexOf("\n", startIdx);

        if (endIdx == -1) {
            endIdx = test.length(); // '\n'이 없으면 문자열 끝까지
        }

        return test.substring(startIdx, endIdx).trim(); // Duration 값을 추출하여 리턴
    }

    // test 문자열에서 BPM 값 추출
    private static int extractBPM(String analysisData) {
        String bpmPrefix = "BPM Data :";
        int startIdx = analysisData.indexOf(bpmPrefix);

        // BPM 데이터 시작 위치를 찾아서 그 뒤에 있는 값을 추출
        startIdx += bpmPrefix.length();
        int endIdx = analysisData.indexOf("bpm", startIdx);

        if (endIdx == -1) {
            endIdx = analysisData.length(); // '\n'이 없으면 문자열 끝까지
        }

        // 소수 버리고 정수만
        return (int) Double.parseDouble(analysisData.substring(startIdx, endIdx).trim());
    }

}
