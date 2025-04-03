package org.musicshare.domain.like.model.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.musicshare.domain.like.model.Like;
import org.musicshare.global.entity.TimeBaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "likes")
public class LikeEntity extends TimeBaseEntity {
    @EmbeddedId
    private LikeIdEntity id;

    public LikeEntity(Like like) {
        this.id = new LikeIdEntity(like.getTargetId(), like.getMember().getId(), like.getTargetType());
    }
}