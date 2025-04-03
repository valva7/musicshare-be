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
@AllArgsConstructor
public class MusicFile {

    private Long id;
    private Music music;
    private MusicFileInfo info;

}
