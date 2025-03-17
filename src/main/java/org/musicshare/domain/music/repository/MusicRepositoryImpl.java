package org.musicshare.domain.music.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.musicshare.domain.music.model.Music;
import org.musicshare.domain.music.model.entity.MusicEntity;
import org.springframework.stereotype.Repository;
import org.musicshare.domain.music.model.entity.QMusicEntity;
import org.musicshare.domain.music.model.entity.QMusicFileEntity;
import org.musicshare.domain.member.model.entity.QMemberEntity;

@Repository
@AllArgsConstructor
public class MusicRepositoryImpl implements MusicRepository{

    private final JpaMusicRepository jpaMusicRepository;

    private final JPAQueryFactory queryFactory;

    private static final QMusicEntity music = QMusicEntity.musicEntity;
    private static final QMusicFileEntity musicFile = QMusicFileEntity.musicFileEntity;
    private static final QMemberEntity member = QMemberEntity.memberEntity;

    public Music findMusicById(Long id) {
        MusicEntity music = jpaMusicRepository.findById(id).orElseThrow();
        return music.toMusic();
    }

}
