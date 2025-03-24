package org.musicshare.domain.music.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class MusicInfo {

    private String title;
    private String theme;
    private String mood;
    private String genre;
    private String tags;
    private int bpm;
    private String duration;
    private String description;
    private PositiveDoubleCounter rating;
    private PositiveIntegerCounter downloadCount;
    private PositiveIntegerCounter likeCount;
    private PositiveIntegerCounter commentCount;

    public MusicInfo(String title, String theme, String mood, String genre, String tags, int bpm, String duration, String description, double rating, int downloadCount, int likeCount, int commentCount) {
        this.title = title;
        this.theme = theme;
        this.mood = mood;
        this.genre = genre;
        this.tags = tags;
        this.bpm = bpm;
        this.duration = duration;
        this.description = description;
        this.rating = new PositiveDoubleCounter(rating);
        this.downloadCount = new PositiveIntegerCounter(downloadCount);
        this.likeCount = new PositiveIntegerCounter(likeCount);
        this.commentCount = new PositiveIntegerCounter(commentCount);
    }

}
