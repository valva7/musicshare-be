package org.musicshare.domain.member.repository;

import org.musicshare.domain.member.model.entity.FanEntity;
import org.musicshare.domain.member.model.entity.FanIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFanRepository extends JpaRepository<FanEntity, FanIdEntity> {

}
