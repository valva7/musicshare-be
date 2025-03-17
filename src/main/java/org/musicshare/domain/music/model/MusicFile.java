package org.musicshare.domain.music.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class MusicFile {

    private Long id;
    private Music music;
    private MusicFileInfo info;

    public MusicFile(Long id, Music music, MusicFileInfo info) {
        this.id = id;
        this.music = music;
        this.info = info;
    }

}
