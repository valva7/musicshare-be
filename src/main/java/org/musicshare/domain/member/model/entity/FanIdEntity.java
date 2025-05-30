package org.musicshare.domain.member.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class FanIdEntity {
    @Column(name = "artist_id")
    private Long artistId;

    @Column(name = "fan_id")
    private Long fanId;
}