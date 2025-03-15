package org.musicshare.domain.music.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import org.musicshare.domain.member.model.entity.MemberEntity;
import org.musicshare.domain.music.model.Music;
import org.musicshare.global.entity.TimeBaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "music")
public class MusicEntity extends TimeBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private MemberEntity author;

    @Column(nullable = false)
    private String title;

    private String theme;
    private String mood;
    private String genre;
    private String tags;
    private String duration;
    private String description;
    private double rating;
    private int downloadCount;
    private int likeCount;

    public MusicEntity(Music music) {
        this.id = music.getId();
        this.author = new MemberEntity(music.getAuthor());
        this.title = music.getInfo().getTitle();
        this.theme = music.getInfo().getTheme();
        this.genre = music.getInfo().getGenre();
        this.duration = music.getInfo().getDuration();
        this.description = music.getInfo().getDescription();
        this.rating = music.getInfo().getRating();
        this.downloadCount = music.getInfo().getDownloadCount();
        this.likeCount = music.getInfo().getLikeCount();
    }

}