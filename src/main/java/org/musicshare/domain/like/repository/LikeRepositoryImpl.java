package org.musicshare.domain.like.repository;

import lombok.RequiredArgsConstructor;
import org.musicshare.domain.like.model.Like;
import org.musicshare.domain.like.model.entity.LikeEntity;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepository {

    private final JpaLikeRepository jpaLikeRepository;

    public boolean checkLike(Like like) {
        LikeEntity entity = new LikeEntity(like);
        return jpaLikeRepository.existsById(entity.getId());
    }

}
