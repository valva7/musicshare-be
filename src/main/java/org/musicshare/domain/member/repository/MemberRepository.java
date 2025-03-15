package org.musicshare.domain.member.repository;

import org.musicshare.domain.member.model.Member;

public interface MemberRepository {

    Member findMemberByNickname(String nickname);

    Member findMemberById(Long id);
}
