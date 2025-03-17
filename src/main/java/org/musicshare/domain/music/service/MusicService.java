package org.musicshare.domain.music.service;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.musicshare.common.utils.FFmpegAudioAnalysis;
import org.musicshare.domain.ai.dto.res.GptRes;
import org.musicshare.domain.ai.service.OpenAiService;
import org.musicshare.domain.file.service.S3Uploader;
import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.music.dto.res.TopTenMusicCurrentRes;
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
    private final JpaMusicFileRepository jpaMusicFileRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public void uploadMusicFile(UserAuth userAuth, MultipartFile file, String title, String description, String genre, String theme, String tags) throws Exception {
        String uploadUrl = "";
        try {
            Member member = memberRepository.findMemberById(userAuth.getUserId());
            // 음악 분석 및 음악정보 생성
            MusicInfo musicInfo = analyzeMusic(file, title, description, genre, theme, tags);
            Music music = saveMusic(musicInfo, member);

            // 음악 파일 S3 업로드
            uploadUrl = uploadMusicFileToS3(file);

            saveMusicFile(file, music, uploadUrl);
        } catch (DataIntegrityViolationException | PersistenceException e) {
            // JPA 예외 발생 시 S3에 업로드한 파일 삭제
            handleException(uploadUrl);
        }
    }

    public List<TopTenMusicCurrentRes> getTop10ByCurrentMonthOrWeekOrderByLikes(){
        return jpaMusicRepository.findTop10ByCurrentMonthOrWeekOrderByLikes();
    }

    // ============================================================ Inner Method ============================================================
    private MusicInfo analyzeMusic(MultipartFile file, String title, String description, String genre, String theme, String tags) throws Exception {
        // FFMPEG으로 음악 특징 분석
        String musicAnalysisData = FFmpegAudioAnalysis.analyzeAudio(file);

        // AI에게 음악 분위기 분석 요청
        GptRes gptRes = openAiService.analyzeMusicWithGPT(musicAnalysisData);

        // 음악 분석 정보
        String duration = musicAnalysisData.substring(musicAnalysisData.indexOf("Duration Data :") + "Duration Data :".length(), musicAnalysisData.indexOf("\n"));
        String mood = gptRes.getChoices().get(0).getMessage().getContent();

        return new MusicInfo(title, theme, mood, genre, tags, duration, description, 0, 0, 0);
    }

    private String uploadMusicFileToS3(MultipartFile file) throws IOException {
        try {
            // 음악 파일 S3 업로드
            return s3Uploader.musicFileUpload(file);
        } catch (IOException e) {
            throw new IOException("파일 업로드 실패", e);
        }
    }

    private Music saveMusic(MusicInfo musicInfo, Member member) {
        Music music = new Music(null, musicInfo, member);
        MusicEntity musicEntity = new MusicEntity(music);
        jpaMusicRepository.save(musicEntity);
        music.setId(musicEntity.getId());
        return music;
    }

    private void saveMusicFile(MultipartFile file, Music music, String uploadUrl) {
        MusicFileInfo musicFileInfo = new MusicFileInfo(file.getOriginalFilename(), null, uploadUrl);
        MusicFile musicFile = new MusicFile(null, music, musicFileInfo);
        MusicFileEntity musicFileEntity = new MusicFileEntity(musicFile);
        jpaMusicFileRepository.save(musicFileEntity);
    }

    private void handleException(String uploadUrl) {
        // S3에 업로드한 파일 삭제
        if (uploadUrl != null && !uploadUrl.isEmpty()) {
            s3Uploader.deleteFile(uploadUrl);
        }
        log.error("데이터 저장 중 오류 발생. S3에서 파일 삭제: {}", uploadUrl);
    }

}
