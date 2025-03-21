package org.musicshare.domain.music.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.music.dto.req.CreateCommentReq;
import org.musicshare.domain.music.dto.res.TopTenMusicCommentRes;
import org.musicshare.domain.music.model.Comment;
import org.musicshare.domain.music.model.Music;
import org.musicshare.domain.music.model.entity.CommentEntity;
import org.musicshare.domain.music.repository.JpaCommentRepository;
import org.musicshare.domain.music.repository.MusicRepository;
import org.musicshare.global.pricipal.UserAuth;
import org.springframework.stereotype.Service;
import org.musicshare.domain.member.repository.MemberRepository;
import org.musicshare.domain.music.repository.JpaMusicRepository;

@Service
@AllArgsConstructor
public class CommentService {

    private final JpaMusicRepository jpaMusicRepository;
    private final MusicRepository musicRepository;
    private final JpaCommentRepository jpaCommentRepository;
    private final MemberRepository memberRepository;

    public void createComment(UserAuth user, CreateCommentReq req) {
        Member member = memberRepository.findMemberById(user.getUserId());
        Music music = musicRepository.findMusicById(req.musicId());

        Comment comment = new Comment(null, member, music, req.content(), req.rating());
        CommentEntity commentEntity = new CommentEntity(comment);

        jpaCommentRepository.save(commentEntity);
    }

    public List<TopTenMusicCommentRes> getTopTenSelectMusicCommentList(Long musicId) {
        return jpaCommentRepository.findCommentsByMusicId(musicId);
    }

}
