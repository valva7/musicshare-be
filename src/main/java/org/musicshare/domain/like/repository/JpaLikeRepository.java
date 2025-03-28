package org.musicshare.domain.like.repository;

import org.musicshare.domain.like.model.entity.LikeEntity;
import org.musicshare.domain.like.model.entity.LikeIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaLikeRepository extends JpaRepository<LikeEntity, LikeIdEntity> {



}
