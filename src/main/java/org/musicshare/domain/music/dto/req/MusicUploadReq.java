package org.musicshare.domain.music.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "음악 업로드 요청")
public record MusicUploadReq(

    @Schema(description = "음악 파일", required = true)
    MultipartFile file,

    @Schema(description = "제목", type = "String", required = true)
    String title,

    @Schema(
        description = "장르 : #GENRE[발라드(BD), 클래식(CS), 댄스(DC), 일렉트로닉(EL), 힙합(HP), R&B(RB), 락(RK)]",
        type = "String",
        allowableValues = {"BD", "CS", "DC", "EL", "HP", "RB", "RK"},
        required = true
    )
    String genre,

    @Schema(
        description = "테마 : #THEME[우주(SP), 자연(NT), 일상(NM), 영화(MV), 사랑(LV)]",
        type = "String",
        allowableValues = {"SP", "NT", "NM", "MV", "LV"},
        required = true
    )
    String theme,

    @Schema(description = "설명", type = "String", required = true)
    String description,

    @Schema(description = "태그", type = "String", required = true)
    String tags

) {}