package org.musicshare.domain.music.repository;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.musicshare.domain.music.dto.res.TopTenMusicCommentRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.musicshare.domain.music.model.entity.CommentEntity;
import org.springframework.data.jpa.repository.Query;

public interface JpaCommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query("SELECT new org.musicshare.domain.music.dto.res.TopTenMusicCommentRes("
        + "c.id, "
        + "mb.id, "
        + "mb.nickname, "
        + "mb.profileImageUrl, "
        + "c.content, "
        + "c.likeCount, "
        + "c.regDt) " +
        "FROM CommentEntity c " +
        "LEFT JOIN MusicEntity m ON c.music.id = m.id " +
        "LEFT JOIN MemberEntity mb ON c.author.id = mb.id " +
        "WHERE m.id = :musicId " +
        "ORDER BY c.likeCount DESC")
    List<TopTenMusicCommentRes> findCommentsByMusicId(@Param("musicId") Long musicId);

}
