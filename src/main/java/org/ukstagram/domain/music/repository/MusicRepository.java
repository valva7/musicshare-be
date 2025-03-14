package org.ukstagram.domain.music.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ukstagram.domain.music.model.entity.MusicEntity;

public interface MusicRepository extends JpaRepository<MusicEntity, Long> {

}
