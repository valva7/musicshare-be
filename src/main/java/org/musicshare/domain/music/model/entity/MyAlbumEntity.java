package org.musicshare.domain.music.model.entity;


import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.musicshare.domain.member.model.entity.MemberEntity;
import org.musicshare.global.entity.TimeBaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "my_album")
public class MyAlbumEntity extends TimeBaseEntity {

    @EmbeddedId
    private MyAlbumIdEntity id;

    @ManyToOne
    @MapsId("memberId")  // MyAlbumIdEntity.memberId에 매핑
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @ManyToOne
    @MapsId("musicId")  // MyAlbumIdEntity.musicId에 매핑
    @JoinColumn(name = "music_id", nullable = false)
    private MusicEntity music;

}
