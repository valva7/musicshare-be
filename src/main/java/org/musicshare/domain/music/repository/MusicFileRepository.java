package org.musicshare.domain.music.repository;

import org.musicshare.domain.music.model.Music;

public interface MusicFileRepository {

    void save(String fileName, Music music, String uploadUrl);

}
