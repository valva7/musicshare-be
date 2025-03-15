package org.musicshare.domain.music.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.musicshare.domain.member.model.Member;

@Builder
@Getter
@AllArgsConstructor
public class Comment {

    private final Long id;
    private final Member author;
    private final Music music;
    private final String content;
    private final PositiveIntegerCounter likeCount;

    public Comment(Long id, Member author, Music music, String content) {
        if(author == null) {
            throw new IllegalArgumentException();
        }
        if(music == null) {
            throw new IllegalArgumentException();
        }

        this.id = id;
        this.author = author;
        this.music = music;
        this.content = content;
        this.likeCount = new PositiveIntegerCounter();
    }

}
