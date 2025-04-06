package org.musicshare.domain.music.service;

import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.music.dto.req.CreateCommentReq;
import org.musicshare.domain.music.dto.res.TopTenMusicCommentRes;
import org.musicshare.domain.music.model.Comment;
import org.musicshare.domain.music.model.Music;
import org.musicshare.domain.music.model.entity.CommentEntity;
import org.musicshare.domain.music.repository.JpaCommentRepository;
import org.musicshare.domain.music.repository.MusicRepository;
import org.musicshare.global.pricipal.UserAuth;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.musicshare.domain.member.repository.MemberRepository;
import org.musicshare.domain.music.repository.JpaMusicRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final JpaMusicRepository jpaMusicRepository;
    private final MusicRepository musicRepository;
    private final JpaCommentRepository jpaCommentRepository;
    private final MemberRepository memberRepository;

    @Retryable(
        value = ObjectOptimisticLockingFailureException.class,
        maxAttempts = 3,
        backoff = @Backoff(delay = 100)
    )
    @Transactional
    public void createComment(UserAuth user, CreateCommentReq req) {
        Member member = memberRepository.findMemberById(user.userId());
        Music music = musicRepository.findMusicById(req.musicId());

        // 댓글 카운트 증가
        int increaseCommentCount = music.getInfo().getCommentCount().increase();
        // 평점 반영
        music.getInfo().getRating().average(req.rating(), increaseCommentCount);
        // 음악 정보 업데이트
        musicRepository.update(music);

        // TODO: 하나의 음악에 한 사람이 여러 댓글을 작성할 수 있게 할지 고민중..

        Comment comment = new Comment(null, member, music, req.content(), req.rating());
        CommentEntity commentEntity = new CommentEntity(comment);

        jpaCommentRepository.save(commentEntity);
    }

    public List<TopTenMusicCommentRes> getMusicCommentList(Long musicId) {
        return jpaCommentRepository.findCommentsByMusicId(musicId);
    }

    @Recover
    public String recover(OptimisticLockException e, Long memberId, Long musicId) {
        log.warn("댓글 작성 실패. memberId={}, musicId={}", memberId, musicId);
        return "댓글 작성이 실패했습니다.";
    }

}
