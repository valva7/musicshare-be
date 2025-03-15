package org.musicshare.domain.music.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.musicshare.domain.member.model.Member;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class Music {

    private Long id;
    private MusicInfo info;
    private Member author;
    private Comment comment;

    public Music(Long id, MusicInfo info, Member author, Comment comment) {
        this.id = id;
        this.info = info;
        this.author = author;
        this.comment = comment;
    }

}
