package org.musicshare.domain.music.repository;

import java.util.List;
import org.musicshare.domain.music.dto.res.TopTenMusicCommentRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.musicshare.domain.music.model.entity.CommentEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaCommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query("SELECT new org.musicshare.domain.music.dto.res.TopTenMusicCommentRes("
        + "c.id, "
        + "mb.id, "
        + "mb.nickname, "
        + "mb.profileImageUrl, "
        + "c.content, "
        + "c.rating, "
        + "c.likeCount, "
        + "c.regDt) " +
        "FROM CommentEntity c " +
        "LEFT JOIN MusicEntity m ON c.music.id = m.id " +
        "LEFT JOIN MemberEntity mb ON c.author.id = mb.id " +
        "WHERE m.id = :musicId " +
        "ORDER BY c.id ASC")
    List<TopTenMusicCommentRes> findCommentsByMusicId(@Param("musicId") Long musicId);

}
