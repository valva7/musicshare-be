package org.musicshare.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.musicshare.domain.member.model.entity.MemberEntity;

public interface JpaMemberRepository extends JpaRepository<MemberEntity, Long> {

    MemberEntity findByNickname(String nickname);

    MemberEntity findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);


}
