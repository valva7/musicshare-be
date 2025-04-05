package org.musicshare.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.musicshare.domain.member.dto.req.MemberRes;
import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.member.repository.MemberRepository;
import org.musicshare.global.pricipal.UserAuth;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberRes getMember(UserAuth user) {
        Member member = memberRepository.findMemberById(user.getUserId());
        return new MemberRes(member);
    }

}
