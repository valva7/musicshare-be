package org.musicshare.domain.music.model;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
public class MusicInfo {

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

    public MusicInfo(String title, String theme, String mood, String genre, String tags, int bpm, String duration, String description, double rating, int downloadCount, int likeCount) {
        this.title = title;
        this.theme = theme;
        this.mood = mood;
        this.genre = genre;
        this.tags = tags;
        this.bpm = bpm;
        this.duration = duration;
        this.description = description;
        this.rating = rating;
        this.downloadCount = downloadCount;
        this.likeCount = likeCount;
    }

}
