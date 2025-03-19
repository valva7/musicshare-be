package org.musicshare.domain.music.repository;

import org.musicshare.domain.music.dto.res.MusicDetailRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.musicshare.domain.music.model.entity.MusicEntity;

public interface JpaMusicRepository extends JpaRepository<MusicEntity, Long> {

    @Query("SELECT new org.musicshare.domain.music.dto.res.MusicDetailRes(" +
        "m.id, " +
        "m.author.id, " +
        "m.author.nickname, " +
        "m.author.profileImageUrl, " +
        "m.title, " +
        "m.theme, " +
        "m.mood, " +
        "m.genre, " +
        "m.duration, " +
        "m.bpm, " +
        "m.rating) " +
        "FROM MusicEntity m " +
        "LEFT JOIN MusicFileEntity mf ON m = mf.music " +
        "LEFT JOIN MemberEntity member ON m.author = member " +
        "WHERE m.id = :musicId ")
    MusicDetailRes findMusicById(Long musicId);

}
