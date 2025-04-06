package org.musicshare.domain.music.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.music.dto.res.PopularMusicRes;
import org.musicshare.domain.music.model.Music;
import org.musicshare.domain.music.model.MusicInfo;
import org.musicshare.domain.music.model.entity.MusicEntity;
import org.springframework.stereotype.Repository;
import org.musicshare.domain.music.model.entity.QMusicEntity;
import org.musicshare.domain.music.model.entity.QMusicFileEntity;
import org.musicshare.domain.member.model.entity.QMemberEntity;

@Repository
@RequiredArgsConstructor
public class MusicRepositoryImpl implements MusicRepository{

    private final JpaMusicRepository jpaMusicRepository;
    private final EntityManager entityManager;

    private final JPAQueryFactory queryFactory;

    private static final QMusicEntity music = QMusicEntity.musicEntity;
    private static final QMusicFileEntity musicFile = QMusicFileEntity.musicFileEntity;
    private static final QMemberEntity member = QMemberEntity.memberEntity;

    public Music findMusicById(Long id) {
        MusicEntity music = jpaMusicRepository.findById(id).orElseThrow();
        return music.toMusic();
    }

    public Music save(MusicInfo musicInfo, Member member) {
        Music music = new Music(null, musicInfo, member);
        MusicEntity musicEntity = new MusicEntity(music);

        entityManager.persist(musicEntity);
        entityManager.flush();

        music.setId(musicEntity.getId());
        return music;
    }

    public void update(Music music) {
        MusicEntity musicEntity = new MusicEntity(music);

        entityManager.merge(musicEntity);
        entityManager.flush();
    }

    public List<PopularMusicRes> findTop10ByCurrentMonthOrWeekOrderByLikes(String genre) {
        return queryFactory.select(
            Projections.constructor(PopularMusicRes.class,
                member.id.as("memberId"),
                member.nickname.as("nickname"),
                member.profileImageUrl.as("profileImageUrl"),
                music.id.as("musicId"),
                music.title.as("title"),
                music.duration.as("duration"),
                music.genre.as("genre"),
                music.mood.as("mood"),
                musicFile.url.as("url"),
                music.likeCount.as("likeCount")
            ))
            .from(music)
                .leftJoin(musicFile).on(music.id.eq(musicFile.id))
                .leftJoin(member).on(music.author.eq(member))
            .where(
                hasGenre(genre)
            )
            .orderBy(music.likeCount.desc())
            .limit(10)
            .fetch();
    }

    private BooleanExpression hasGenre(String genre) {
        if (genre == null) {
            LocalDate now = LocalDate.now();
            String currentMonth = now.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            int currentWeek = now.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
            int currentYear = now.getYear();

            return music.regDt.year().stringValue().concat("-").concat(music.regDt.month().stringValue()).eq(currentMonth)
                .or(
                    Expressions.numberTemplate(Integer.class, "YEARWEEK({0}, 1)", music.regDt)
                        .eq(currentYear * 100 + currentWeek)
                );
        } else {
            return music.genre.eq(genre);
        }
    }

}
