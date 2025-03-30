package org.musicshare.domain.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.musicshare.domain.member.dto.req.MemberResDto;
import org.musicshare.domain.member.service.MemberService;
import org.musicshare.global.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name="Basics", description = "회원 관리 API")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public Response<MemberResDto> getUser(Long userId){
        return Response.ok(memberService.getMember(userId));
    }

}
