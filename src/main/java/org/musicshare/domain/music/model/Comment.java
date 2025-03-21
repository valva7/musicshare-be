package org.musicshare.domain.music.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.musicshare.domain.member.model.Member;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private Long id;
    private Member author;
    private Music music;
    private String content;
    private int rating;
    private PositiveIntegerCounter likeCount;

    public Comment(Long id, Member author, Music music, String content, int rating) {
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
        this.rating = rating;
        this.likeCount = new PositiveIntegerCounter();
    }

}
