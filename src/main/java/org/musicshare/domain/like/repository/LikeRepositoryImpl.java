package org.musicshare.domain.like.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.musicshare.domain.like.LikeType;
import org.musicshare.domain.like.dto.res.MusicLikedRes;
import org.musicshare.domain.like.model.Like;
import org.musicshare.domain.like.model.entity.LikeEntity;
import org.musicshare.domain.like.model.entity.QLikeEntity;
import org.musicshare.domain.music.model.entity.QMusicEntity;
import org.musicshare.global.pricipal.UserAuth;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {

    private final JPAQueryFactory queryFactory;

    private final JpaLikeRepository jpaLikeRepository;

    QLikeEntity like = QLikeEntity.likeEntity;
    QMusicEntity music = QMusicEntity.musicEntity;

    public boolean checkLike(Like like) {
        LikeEntity entity = new LikeEntity(like);
        return jpaLikeRepository.existsById(entity.getId());
    }

    public MusicLikedRes getMusicLiked(UserAuth user, Long musicId) {
        return queryFactory
            .select(Projections.constructor(MusicLikedRes.class,
                new CaseBuilder()
                    .when(like.count().gt(0))
                    .then(true)
                    .otherwise(false).as("liked"),
                music.likeCount.as("likeCount")
            ))
            .from(music)
            .leftJoin(like)
            .on(like.id.targetId.eq(music.id)
                .and(like.id.memberId.eq(user.getUserId()))
                .and(like.id.targetType.eq(LikeType.MUSIC.getCode()))
            )
            .where(music.id.eq(musicId))
            .fetchOne();
    }

}
