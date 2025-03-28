package org.musicshare.domain.like.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.musicshare.domain.like.LikeType;
import org.musicshare.domain.like.model.Like;
import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.music.model.Comment;
import org.musicshare.domain.music.model.Music;
import org.musicshare.global.entity.TimeBaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "like_entity")
public class LikeEntity extends TimeBaseEntity {
    @EmbeddedId
    private LikeIdEntity id;

    public LikeEntity(Like like) {
        this.id = new LikeIdEntity(like.getTargetId(), like.getMember().getId(), like.getTargetType());
    }
}