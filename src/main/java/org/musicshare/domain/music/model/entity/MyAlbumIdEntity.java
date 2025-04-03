package org.musicshare.domain.music.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MyAlbumIdEntity {
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "music_id")
    private Long musicId;
}
