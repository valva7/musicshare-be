package org.musicshare.domain.music.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.member.model.MemberInfo;
import org.musicshare.domain.member.model.entity.MemberEntity;
import org.musicshare.domain.music.model.Music;
import org.musicshare.domain.music.model.MusicInfo;
import org.musicshare.domain.music.model.PositiveIntegerCounter;
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

    @OneToOne(mappedBy = "music")
    private MusicFileEntity musicFile;

    @ManyToOne(fetch = FetchType.LAZY)
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

    public Music toMusic() {
        return Music.builder()
            .id(this.id)
            .info(new MusicInfo(this.title, this.theme, this.mood, this.genre, this.tags, this.duration, this.description, this.rating, this.downloadCount, this.likeCount))
            .author(new Member(this.author.getId(), new MemberInfo(this.author.getNickname(), this.author.getProfileImageUrl())))
            .likeCount(new PositiveIntegerCounter(this.likeCount))
            .build();
    }

}