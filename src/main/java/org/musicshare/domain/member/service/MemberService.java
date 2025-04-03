package org.musicshare.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.musicshare.domain.member.dto.req.MemberResDto;
import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResDto getMember(Long memberId) {
        Member member = memberRepository.findMemberById(memberId);
        return new MemberResDto(member);
    }

}
