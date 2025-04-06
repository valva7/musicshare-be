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

    public MemberEntity saveMember(Member member){
        MemberEntity memberEntity = new MemberEntity(member);
        return jpaMemberRepository.save(memberEntity);
    }

    public Member findMemberByNickname(String nickname){
        MemberEntity memberEntity = jpaMemberRepository.findByNickname(nickname);
        return memberEntity == null ? null : memberEntity.toMember();
    }

    public Member findMemberById(Long memberId){
        MemberEntity memberEntity = jpaMemberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 사용자"));
        return memberEntity.toMember();
    }

    public Member findMemberByEmail(String email){
        MemberEntity memberEntity = jpaMemberRepository.findByEmail(email);
        return memberEntity == null ? null : memberEntity.toMember();
    }

}
