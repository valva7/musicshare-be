package org.musicshare.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.member.model.entity.MemberEntity;
import org.musicshare.global.exception.MemberNotFoundException;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final JpaMemberRepository jpaMemberRepository;

    public Member findMemberByNickname(String nickname){
        MemberEntity memberEntity = jpaMemberRepository.findByNickname(nickname);
        return memberEntity == null ? null : memberEntity.toMember();
    }

    public Member findMemberById(Long memberId){
        MemberEntity memberEntity = jpaMemberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        return memberEntity.toMember();
    }

}
