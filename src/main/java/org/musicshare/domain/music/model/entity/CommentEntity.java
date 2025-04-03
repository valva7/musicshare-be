package org.musicshare.domain.music.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.musicshare.domain.member.model.entity.MemberEntity;
import org.musicshare.domain.music.model.Comment;
import org.musicshare.global.entity.TimeBaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comment")
public class CommentEntity extends TimeBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private MemberEntity author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "music_id", nullable = false)
    private MusicEntity music;

    @Column(nullable = false, length = 500)
    private String content;

    private int rating;
    private int likeCount;

    public CommentEntity(Comment comment) {
        this.id = comment.getId();
        this.author = new MemberEntity(comment.getAuthor());
        this.music = new MusicEntity(comment.getMusic());
        this.content = comment.getContent();
        this.rating = comment.getRating();
        this.likeCount = 0;
    }

}