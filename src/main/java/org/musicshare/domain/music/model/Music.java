package org.musicshare.domain.music.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.musicshare.domain.member.model.Member;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class Music {

    private Long id;
    private MusicInfo info;
    private Member author;
    private Integer version;

    public Music(Long id, MusicInfo info, Member author) {
        this.id = id;
        this.info = info;
        this.author = author;
        this.version = 0;
    }

}
