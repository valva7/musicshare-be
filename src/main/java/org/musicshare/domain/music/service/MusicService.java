package org.musicshare.domain.music.service;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.musicshare.domain.music.dto.req.MusicUploadReq;
import org.musicshare.domain.music.dto.res.MusicDetailRes;
import org.musicshare.domain.music.repository.MusicFileRepository;
import org.musicshare.domain.music.repository.MusicRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.musicshare.domain.music.utils.FFmpegAudioAnalysis;
import org.musicshare.domain.ai.dto.res.GptRes;
import org.musicshare.domain.ai.service.OpenAiService;
import org.musicshare.domain.file.service.S3Uploader;
import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.music.dto.res.PopularMusicRes;
import org.musicshare.domain.music.model.Music;
import org.musicshare.domain.music.model.MusicInfo;
import org.musicshare.domain.music.repository.JpaMusicRepository;
import org.musicshare.domain.member.repository.MemberRepository;
import org.musicshare.global.pricipal.UserAuth;

@Slf4j
@Service
@RequiredArgsConstructor
public class MusicService {

    private final OpenAiService openAiService;
    private final S3Uploader s3Uploader;

    private final JpaMusicRepository jpaMusicRepository;
    private final MusicRepository musicRepository;
    private final MusicFileRepository musicFileRepository;
    private final MemberRepository memberRepository;


    /**
     * 음악 파일 업로드
     * @param userAuth
     * @param req
     * @throws Exception
     */
    @Transactional
    public void uploadMusicFile(UserAuth userAuth, MusicUploadReq req) throws Exception {
        String uploadUrl = "";
        try {
            Member member = memberRepository.findMemberById(userAuth.userId());

            // 음악 분석 및 음악정보 생성
            MusicInfo musicInfo = analyzeMusic(req);

            // 음악 파일 S3 업로드
            uploadUrl = uploadMusicFileToS3(req.file());

            // 음악 정보 저장
            Music music = musicRepository.save(musicInfo, member);
            // 음악 파일 정보 저장
            musicFileRepository.save(req.file().getOriginalFilename(), music, uploadUrl);
        } catch (DataIntegrityViolationException | PersistenceException e) {
            // 예외 발생 시 S3에 업로드한 파일 삭제
            log.error("데이터 저장 중 오류 발생. S3에서 파일 삭제: {}", uploadUrl);
            if (uploadUrl != null && !uploadUrl.isEmpty()) {
                s3Uploader.deleteFile(uploadUrl);
            }
        }
    }

    /**
     * Top 10 음악 조회
     * @param genre
     */
    public List<PopularMusicRes> getTop10ByCurrentMonthOrWeekOrderByLikes(String genre) {
        return musicRepository.findTop10ByCurrentMonthOrWeekOrderByLikes(genre);
    }

    /**
     * 음악 세부 정보 조회
     * @param musicId
     * @return
     */
    public MusicDetailRes getMusicDetail(Long musicId){
        return jpaMusicRepository.findMusicById(musicId);
    }

    // ============================================================ Inner Method ============================================================
    private MusicInfo analyzeMusic(MusicUploadReq req) throws Exception {
        // FFMPEG으로 음악 특징 분석
        String musicAnalysisData = FFmpegAudioAnalysis.analyzeAudio(req.file());

        // AI에게 음악 분위기 분석 요청
        GptRes gptRes = openAiService.analyzeMusicWithGPT(musicAnalysisData);

        // 음악 분석 정보
        String duration = extractDuration(musicAnalysisData);
        int bpm = extractBPM(musicAnalysisData);
        String mood = gptRes.getChoices().get(0).getMessage().getContent();

        return new MusicInfo(req.title(), req.theme(), mood, req.genre(), req.tags(), bpm, duration, req.description());
    }

    private String uploadMusicFileToS3(MultipartFile file) throws IOException {
        try {
            // 음악 파일 S3 업로드
            return s3Uploader.musicFileUpload(file);
        } catch (IOException e) {
            throw new IOException("파일 업로드 실패", e);
        }
    }

    // 문자열에서 Duration Data 추출
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

    // 문자열에서 BPM 값 추출
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
