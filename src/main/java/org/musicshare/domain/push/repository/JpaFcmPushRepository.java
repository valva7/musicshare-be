package org.musicshare.domain.push.repository;

import org.musicshare.domain.push.entity.FcmTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFcmPushRepository extends JpaRepository<FcmTokenEntity, Long> {

}
