package org.calendaralarm.domain.user.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.calendaralarm.domain.user.model.dto.req.CalendarUserResDto;
import org.calendaralarm.domain.user.service.CalendarUserService;
import org.calendaralarm.global.dto.Response;
import org.calendaralarm.global.pricipal.AuthPrincipal;
import org.calendaralarm.global.pricipal.UserPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name="Basics", description = "회원 관리 API")
public class UserController {

    private final CalendarUserService calendarUserService;

    @GetMapping
    public Response<CalendarUserResDto> getUser(@Parameter(hidden = true)@AuthPrincipal UserPrincipal user, Long userId){
        return Response.ok(calendarUserService.getUser(userId));
    }

}
