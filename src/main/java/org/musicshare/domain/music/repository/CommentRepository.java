package org.musicshare.domain.music.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.musicshare.domain.music.model.entity.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

}
