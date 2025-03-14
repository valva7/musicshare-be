package org.ukstagram.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.ukstagram.domain.user.dto.req.CalendarUserResDto;
import org.ukstagram.domain.user.model.entity.MemberEntity;
import org.ukstagram.domain.user.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CalendarUserService {

    private final MemberRepository memberRepository;

    public CalendarUserResDto getUser(Long userId) {
        MemberEntity entity = memberRepository.getCalendarUserEntityById(userId);
        return new CalendarUserResDto(entity);
    }

}
