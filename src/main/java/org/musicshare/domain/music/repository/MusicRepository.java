package org.musicshare.domain.music.repository;

import java.util.List;
import org.musicshare.domain.music.dto.res.PopularMusicRes;
import org.musicshare.domain.music.model.Music;

public interface MusicRepository {

    Music findMusicById(Long id);

    List<PopularMusicRes> findTop10ByCurrentMonthOrWeekOrderByLikes(String genre);

}
