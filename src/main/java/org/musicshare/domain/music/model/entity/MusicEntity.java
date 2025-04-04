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
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.member.model.MemberInfo;
import org.musicshare.domain.member.model.entity.MemberEntity;
import org.musicshare.domain.music.model.Music;
import org.musicshare.domain.music.model.MusicInfo;
import org.musicshare.global.entity.TimeBaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
    private int bpm;
    private String duration;
    private String description;
    private double rating;
    private int downloadCount;
    private int likeCount;
    private int commentCount;

    @Version  // 버전 필드 (낙관락)
    private Integer version;

    public MusicEntity(Music music) {
        this.id = music.getId();
        this.author = new MemberEntity(music.getAuthor());
        this.title = music.getInfo().getTitle();
        this.theme = music.getInfo().getTheme();
        this.mood = music.getInfo().getMood();
        this.genre = music.getInfo().getGenre();
        this.tags = music.getInfo().getTags();
        this.bpm = music.getInfo().getBpm();
        this.duration = music.getInfo().getDuration();
        this.description = music.getInfo().getDescription();
        this.rating = music.getInfo().getRating().getCount();
        this.downloadCount = music.getInfo().getDownloadCount().getCount();
        this.likeCount = music.getInfo().getLikeCount().getCount();
        this.commentCount = music.getInfo().getCommentCount().getCount();
        this.version = music.getVersion();
    }

    public Music toMusic() {
        return Music.builder()
            .id(this.id)
            .info(new MusicInfo(this.title, this.theme, this.mood, this.genre, this.tags, this.bpm, this.duration, this.description, this.rating, this.downloadCount, this.likeCount, this.commentCount))
            .author(new Member(this.author.getId(), new MemberInfo(this.author.getEmail(), this.author.getNickname(), this.author.getProfileImageUrl())))
            .version(this.version)
            .build();
    }

}