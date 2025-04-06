package org.musicshare.domain.music.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.musicshare.global.annotation.FileValidate;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "음악 업로드 요청")
public record MusicUploadReq(

    @Schema(description = "음악 파일", type = "file")
    @FileValidate(allowedTypes = {"mp3", "wav"}, fileType = "MF", message = "mp3 또는 wav 파일만 업로드 가능합니다.")
    MultipartFile file,

    @Schema(description = "제목", type = "String")
    @NotBlank(message = "제목은 필수입니다.")
    String title,

    @Schema(
        description = "장르 : #GENRE[발라드(BD), 클래식(CS), 댄스(DC), 일렉트로닉(EL), 힙합(HP), R&B(RB), 락(RK)]",
        type = "String",
        allowableValues = {"BD", "CS", "DC", "EL", "HP", "RB", "RK"}
    )
    @NotBlank(message = "장르는 필수입니다.")
    @Pattern(regexp = "BD|CS|DC|EL|HP|RB|RK", message = "올바른 장르 코드여야 합니다.")
    String genre,

    @Schema(
        description = "테마 : #THEME[우주(SP), 자연(NT), 일상(NM), 영화(MV), 사랑(LV)]",
        type = "String",
        allowableValues = {"SP", "NT", "NM", "MV", "LV"}
    )
    @NotBlank(message = "테마는 필수입니다.")
    @Pattern(regexp = "SP|NT|NM|MV|LV", message = "올바른 테마 코드여야 합니다.")
    String theme,

    @Schema(description = "설명", type = "String")
    @NotBlank(message = "설명은 필수입니다.")
    String description,

    @Schema(description = "태그", type = "String")
    @NotBlank(message = "태그는 필수입니다.")
    String tags

) {}