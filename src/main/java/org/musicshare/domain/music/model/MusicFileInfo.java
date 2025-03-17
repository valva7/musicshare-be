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
public class MusicFileInfo {

    private String fileOriName;
    private String fileType;
    private String url;

    public MusicFileInfo(String fileOriName, String fileType, String url) {
        if(fileOriName == null || fileOriName.isEmpty()){
            throw new IllegalArgumentException();
        }
        if(fileType == null || fileType==null){
            // 추가 예정
        }
        if(url == null || url.isEmpty()){
            throw new IllegalArgumentException();
        }

        this.fileOriName = fileOriName;
        this.fileType = fileType;
        this.url = url;
    }

}
