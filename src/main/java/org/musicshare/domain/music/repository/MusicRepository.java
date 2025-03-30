package org.musicshare.domain.music.repository;

import java.util.List;
import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.music.dto.res.PopularMusicRes;
import org.musicshare.domain.music.model.Music;
import org.musicshare.domain.music.model.MusicInfo;

public interface MusicRepository {

    Music findMusicById(Long id);

    Music saveMusic(MusicInfo musicInfo, Member member);

    void updateMusic(Music music);

    List<PopularMusicRes> findTop10ByCurrentMonthOrWeekOrderByLikes(String genre);

}
