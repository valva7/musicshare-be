package org.musicshare.domain.member.repository;

import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.member.model.entity.MemberEntity;

public interface MemberRepository {

    MemberEntity saveMember(Member member);

    Member findMemberByNickname(String nickname);

    Member findMemberById(Long memberId);

    Member findMemberByEmail(String email);
}
