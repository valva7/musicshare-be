package org.ukstagram.domain.user.repository;

import java.util.Optional;
import org.ukstagram.domain.user.model.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findCalendarUserEntityByNickname(String nickname);

    MemberEntity getCalendarUserEntityById(Long id);
}
