package org.musicshare.domain.music.repository;

import org.musicshare.domain.music.model.Music;

public interface MusicRepository {

    Music findMusicById(Long id);

}
