package org.ukstagram.domain.music.service;

import jakarta.transaction.Transactional;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.ukstagram.common.utils.MusicFileProcess;
import org.ukstagram.domain.file.service.S3Uploader;
import org.ukstagram.domain.music.model.entity.MusicEntity;
import org.ukstagram.domain.music.model.entity.MusicFileEntity;
import org.ukstagram.domain.music.repository.MusicFileRepository;
import org.ukstagram.domain.music.repository.MusicRepository;
import org.ukstagram.domain.user.model.entity.MemberEntity;
import org.ukstagram.domain.user.repository.MemberRepository;
import org.ukstagram.global.pricipal.UserAuth;

@Slf4j
@Service
@AllArgsConstructor
public class MusicService {

    private final MusicFileRepository musicFileRepository;
    private final MusicRepository musicRepository;
    private final MemberRepository memberRepository;

    private final S3Uploader s3Uploader;

    @Transactional
    public void uploadMusicFile(UserAuth userAuth, MultipartFile file, String title, String description, String genre, String mood, String theme, String tags) throws Exception {
        MemberEntity memberEntity = memberRepository.findById(userAuth.getUserId()).orElseThrow();

        try {
            // Music Insert
            MusicEntity musicEntity = MusicEntity.builder()
                .title(title)
                .genre(genre)
                .theme(theme)
                .mood(mood)
                .tags(tags)
                .description(description)
                .author(memberEntity)
                .duration(MusicFileProcess.getMp3Duration(file))
                .build();
            musicRepository.save(musicEntity);

            // 음악 파일 S3 업로드
            String uploadUrl = s3Uploader.musicFileUpload(file);

            // MusicFile Insert
            MusicFileEntity musicFileEntity = MusicFileEntity.builder()
                .fileOriName(file.getOriginalFilename())
                .url(uploadUrl)
                .music(musicEntity)
                .build();
            musicFileRepository.save(musicFileEntity);
        } catch (IOException e) {
            throw new IOException();
        }
    }

}
