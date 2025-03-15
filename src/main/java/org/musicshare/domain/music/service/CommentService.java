package org.musicshare.domain.music.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.musicshare.domain.music.repository.CommentRepository;
import org.musicshare.domain.member.repository.MemberRepository;
import org.musicshare.domain.music.repository.JpaMusicRepository;

@Service
@AllArgsConstructor
public class CommentService {

    private final JpaMusicRepository jpaMusicRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

//    public void createComment(UserAuth user, CreateCommentReq req) {
//
//        Member member = memberRepository.findMemberById(user.getUserId());
//        //Music music = jpaMusicRepository.
//
//        commentRepository.save();
//    }

}
