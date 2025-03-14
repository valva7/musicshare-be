package org.ukstagram.domain.music.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ukstagram.domain.music.model.entity.MusicFileEntity;

public interface MusicFileRepository extends JpaRepository<MusicFileEntity, Long> {

}
