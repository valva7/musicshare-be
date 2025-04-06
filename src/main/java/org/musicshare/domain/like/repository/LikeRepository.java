package org.musicshare.domain.like.repository;

import org.musicshare.domain.like.dto.res.MusicLikedRes;
import org.musicshare.domain.like.model.Like;
import org.musicshare.global.pricipal.UserAuth;

public interface LikeRepository {

    boolean checkLike(Like like);

    MusicLikedRes findMusicLiked(UserAuth user, Long musicId);

}
