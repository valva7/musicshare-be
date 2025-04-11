package org.musicshare.domain.music.repository;

import jakarta.persistence.EntityManager;
import org.musicshare.domain.music.model.Music;
import org.musicshare.domain.music.model.MusicFile;
import org.musicshare.domain.music.model.MusicFileInfo;
import org.musicshare.domain.music.model.entity.MusicFileEntity;
import org.springframework.stereotype.Repository;

@Repository
public class MusicFileRepositoryImpl implements MusicFileRepository {

    private final EntityManager entityManager;
    private final JpaMusicFileRepository jpaMusicFileRepository;

    public MusicFileRepositoryImpl(EntityManager entityManager, JpaMusicFileRepository jpaMusicFileRepository) {
        this.entityManager = entityManager;
        this.jpaMusicFileRepository = jpaMusicFileRepository;
    }

    public void save(String fileName, Music music, String uploadUrl) {
        MusicFileInfo musicFileInfo = new MusicFileInfo(fileName, null, uploadUrl);
        MusicFile musicFile = new MusicFile(null, music, musicFileInfo);
        MusicFileEntity musicFileEntity = new MusicFileEntity(musicFile);

        entityManager.persist(musicFileEntity);
        entityManager.flush();
    }

}
