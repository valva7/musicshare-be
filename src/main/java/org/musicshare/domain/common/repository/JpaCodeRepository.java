package org.musicshare.domain.common.repository;

import org.musicshare.domain.common.model.entity.CodeEntity;
import org.musicshare.domain.common.model.entity.CodeIdEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCodeRepository extends JpaRepository<CodeEntity, CodeIdEntity> {

}
