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
import org.musicshare.domain.music.model.MusicFile;
import org.musicshare.global.entity.TimeBaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "music_file")
public class MusicFileEntity extends TimeBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "music_id", nullable = false)
    private MusicEntity music;

    @Column(nullable = false)
    private String fileOriName;

    @Column(nullable = true)
    private String fileType;

    @Column(nullable = false)
    private String url;

    public MusicFileEntity(MusicFile musicFile) {
        this.id = musicFile.getId();
        this.music = new MusicEntity(musicFile.getMusic());
        this.fileOriName = musicFile.getInfo().getFileOriName();
        this.fileType = musicFile.getInfo().getFileType();
        this.url = musicFile.getInfo().getUrl();
    }

}