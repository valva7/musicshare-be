package org.musicshare.domain.music.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.musicshare.domain.music.model.entity.MusicFileEntity;

public interface MusicFileRepository extends JpaRepository<MusicFileEntity, Long> {

}
