package org.musicshare.domain.music.repository;

import java.util.List;
import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.music.dto.res.PopularMusicRes;
import org.musicshare.domain.music.model.Music;
import org.musicshare.domain.music.model.MusicInfo;

public interface MusicRepository {

    Music findMusicById(Long id);

    Music save(MusicInfo musicInfo, Member member);

    void update(Music music);

    List<PopularMusicRes> findTop10ByCurrentMonthOrWeekOrderByLikes(String genre);

}
