package org.musicshare.domain.like.service;

import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.musicshare.domain.like.LikeType;
import org.musicshare.domain.like.dto.res.MusicLikedRes;
import org.musicshare.domain.like.model.Like;
import org.musicshare.domain.like.model.entity.LikeEntity;
import org.musicshare.domain.like.repository.JpaLikeRepository;
import org.musicshare.domain.like.repository.LikeRepository;
import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.member.repository.MemberRepository;
import org.musicshare.domain.music.model.Music;
import org.musicshare.domain.music.repository.MusicRepository;
import org.musicshare.domain.push.repository.FcmPushRepository;
import org.musicshare.domain.push.service.FcmPushService;
import org.musicshare.global.pricipal.UserAuth;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {

    private final FcmPushService fcmPushService;

    private final JpaLikeRepository jpaLikeRepository;
    private final LikeRepository likeRepository;
    private final MusicRepository musicRepository;
    private final MemberRepository memberRepository;
    private final FcmPushRepository fcmPushRepository;

    @Retryable(
        value = ObjectOptimisticLockingFailureException.class,
        maxAttempts = 3,
        backoff = @Backoff(delay = 100)
    )
    @Transactional
    public void likeMusic(UserAuth user,  Long musicId) {
        Music music = musicRepository.findMusicById(musicId);
        Member member = memberRepository.findMemberById(user.getUserId());

        Like like = new Like(member, musicId, LikeType.MUSIC.getCode());
        // 좋아요 여부 확인
        boolean check = likeRepository.checkLike(like);
        if (check) {
            music.getInfo().getLikeCount().decrease();
            deleteLike(like);
        } else {
            music.getInfo().getLikeCount().increase();
            saveLike(like);

            // 좋아요 알림
            fcmPushService.sendLikeMessage(member, music.getAuthor());
        }

        musicRepository.updateMusic(music);
    }

    public MusicLikedRes getMusicLiked(UserAuth user, Long musicId) {
        return likeRepository.getMusicLiked(user, musicId);
    }

    // ========================================= Inner Method =========================================
    public void saveLike(Like like) {
        LikeEntity likeEntity = new LikeEntity(like);
        jpaLikeRepository.save(likeEntity);
    }

    public void deleteLike(Like like) {
        LikeEntity likeEntity = new LikeEntity(like);
        jpaLikeRepository.delete(likeEntity);
    }

    @Recover
    public String recover(OptimisticLockException e, Long memberId, Long musicId) {
        log.warn("좋아요 실패. memberId={}, musicId={}", memberId, musicId);
        return "좋아요를 실패했습니다.";
    }

}
