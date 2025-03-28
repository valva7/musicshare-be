package org.musicshare.domain.like.repository;

import org.musicshare.domain.like.model.Like;

public interface LikeRepository {

    boolean checkLike(Like like);

}
