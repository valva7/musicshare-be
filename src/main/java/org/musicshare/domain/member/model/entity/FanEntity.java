package org.musicshare.domain.member.model.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.musicshare.domain.member.model.Fan;
import org.musicshare.global.entity.TimeBaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "fan")
public class FanEntity extends TimeBaseEntity {
    @EmbeddedId
    private FanIdEntity id;

    @ManyToOne
    @MapsId("artistId")  // FanIdEntity.artistId 매핑
    @JoinColumn(name = "artist_id", nullable = false)
    private MemberEntity member;

    @ManyToOne
    @MapsId("fanId")  // FanIdEntity.fanId 매핑
    @JoinColumn(name = "fan_id", nullable = false)
    private MemberEntity fan;

    public FanEntity(Fan fan) {
        this.id = new FanIdEntity(fan.getArtistId(), fan.getFanId());
    }
}