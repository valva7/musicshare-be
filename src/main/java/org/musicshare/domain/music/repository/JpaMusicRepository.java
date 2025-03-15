package org.musicshare.domain.music.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.musicshare.domain.music.dto.res.TopTenMusicCurrentRes;
import org.musicshare.domain.music.model.entity.MusicEntity;

public interface JpaMusicRepository extends JpaRepository<MusicEntity, Long> {

    @Query("SELECT new org.musicshare.domain.music.dto.res.TopTenMusicCurrentRes(" +
        "m.author.id, " +
        "m.author.nickname, " +
        "m.author.profileImageUrl, " +
        "m.id, " +
        "m.title, " +
        "m.duration, " +
        "m.mood, " +
        "mf.url, " +
        "m.likeCount) " +
        "FROM MusicEntity m " +
        "LEFT JOIN MusicFileEntity mf ON m = mf.music " +
        "LEFT JOIN MemberEntity member ON m.author = member " +
        "WHERE FUNCTION('DATE_FORMAT', m.regDt, '%Y-%m') = FUNCTION('DATE_FORMAT', CURRENT_DATE, '%Y-%m') " +
        "OR FUNCTION('YEARWEEK', m.regDt, 1) = FUNCTION('YEARWEEK', CURRENT_DATE, 1) " +
        "ORDER BY m.likeCount DESC " +
        "LIMIT 10")
    List<TopTenMusicCurrentRes> findTop10ByCurrentMonthOrWeekOrderByLikes();

}
