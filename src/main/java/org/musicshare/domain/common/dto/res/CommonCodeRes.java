package org.musicshare.domain.common.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;


public record CommonCodeRes(

    @Schema(description = "코드", type = "String")
    String code,

    @Schema(description = "코드명", type = "String")
    String name

) {

}
