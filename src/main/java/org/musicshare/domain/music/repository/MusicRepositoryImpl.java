package org.musicshare.domain.music.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.musicshare.domain.music.model.entity.QMusicEntity;
import org.musicshare.domain.music.model.entity.QMusicFileEntity;
import org.musicshare.domain.member.model.entity.QMemberEntity;

@Repository
@AllArgsConstructor
public class MusicRepositoryImpl implements MusicRepository{

    private final JPAQueryFactory queryFactory;

    private static final QMusicEntity music = QMusicEntity.musicEntity;
    private static final QMusicFileEntity musicFile = QMusicFileEntity.musicFileEntity;
    private static final QMemberEntity member = QMemberEntity.memberEntity;

}
