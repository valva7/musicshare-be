package org.musicshare.domain.music.repository;

import java.util.List;
import org.musicshare.domain.music.dto.res.TopTenMusicCurrentRes;
import org.musicshare.domain.music.model.Music;

public interface MusicRepository {

    Music findMusicById(Long id);

    List<TopTenMusicCurrentRes> findTop10ByCurrentMonthOrWeekOrderByLikes(String genre);

}
