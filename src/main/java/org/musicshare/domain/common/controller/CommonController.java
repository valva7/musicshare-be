package org.musicshare.domain.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.musicshare.domain.common.dto.res.CommonCodeRes;
import org.musicshare.domain.common.service.CommonService;
import org.musicshare.global.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공통", description = "공통 관련 API")
@RestController
@RequestMapping("/common")
@AllArgsConstructor
public class CommonController {

    private final CommonService commonService;

    @GetMapping("/code/{groupCode}")
    @Parameters({
        @Parameter(name = "codeGroup", description = "공통코드그룹", in = ParameterIn.PATH)
    })
    @Operation(summary = "공통코드조회", description = "공통코드를 조회한다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "공통코드 조회 성공", content = @Content(schema = @Schema(implementation = CommonCodeRes[].class)))
    })
    public Response<List<CommonCodeRes>> getCommonCodeList(@PathVariable("groupCode") String codeGroup) {
        return Response.ok(commonService.getCommonCodeList(codeGroup));
    }

}
