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

    /**
     * 회원 정보 조회
     * @param user
     * @return
     */
    public MemberRes getMember(UserAuth user) {
        Member member = memberRepository.findMemberById(user.userId());
        return new MemberRes(member);
    }

}
