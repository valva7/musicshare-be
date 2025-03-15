package org.musicshare.domain.music.service;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.musicshare.domain.music.repository.MusicFileRepository;
import org.musicshare.domain.music.repository.JpaMusicRepository;
import org.musicshare.domain.member.repository.MemberRepository;
import org.musicshare.global.pricipal.UserAuth;

@Slf4j
@Service
@AllArgsConstructor
public class MusicService {

    private final JpaMusicRepository jpaMusicRepository;
    private final MusicFileRepository musicFileRepository;
    private final MemberRepository memberRepository;

    private final OpenAiService openAiService;

    private final S3Uploader s3Uploader;

    @Transactional
    public void uploadMusicFile(UserAuth userAuth, MultipartFile file, String title, String description, String genre, String theme, String tags) throws Exception {
        Member member = memberRepository.findMemberById(userAuth.getUserId());

        try {
            // FFMPEG으로 음악 특징 분석
            String musicAnalysisData = FFmpegAudioAnalysis.analyzeAudio(file);
            // AI에게 음악 분위기 분석 요청
            GptRes gptRes = openAiService.analyzeMusicWithGPT(musicAnalysisData);

            // 음악 분석 정보
            String duration = musicAnalysisData.substring(musicAnalysisData.indexOf("Duration Data :") + "Duration Data :".length(), musicAnalysisData.indexOf("\n"));
            String mood = gptRes.getChoices().get(0).getMessage().getContent();

            MusicInfo musicInfo = new MusicInfo(title, theme, mood, genre, tags, duration, description, 0, 0, 0);
            Music music = new Music(null, musicInfo, member, null);

            // Music Insert
            MusicEntity musicEntity = new MusicEntity(music);
            jpaMusicRepository.save(musicEntity);

            // 음악 파일 S3 업로드
            String uploadUrl = s3Uploader.musicFileUpload(file);

            MusicFileInfo musicFileInfo = new MusicFileInfo(file.getOriginalFilename(), null, uploadUrl);
            MusicFile musicFile = new MusicFile(null, music, musicFileInfo);

            // MusicFile Insert
            MusicFileEntity musicFileEntity = new MusicFileEntity(musicFile);
            musicFileRepository.save(musicFileEntity);
        } catch (IOException e) {
            throw new IOException();
        }
    }

    public List<TopTenMusicCurrentRes> getTop10ByCurrentMonthOrWeekOrderByLikes(){
        return jpaMusicRepository.findTop10ByCurrentMonthOrWeekOrderByLikes();
    }

}
