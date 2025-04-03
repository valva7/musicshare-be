package org.musicshare.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.musicshare.domain.member.model.Fan;
import org.musicshare.domain.member.repository.FanRepository;
import org.musicshare.global.pricipal.UserAuth;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FanService {

    private final FanRepository fanRepository;

    public void fanArtist(UserAuth user, Long artistId) {
        boolean existFan = fanRepository.existFan(artistId, user.getUserId());

        Fan fan = new Fan(artistId, user.getUserId());
        if(existFan) {
            fanRepository.delete(fan);
        } else {
            fanRepository.save(fan);
        }
    }
}
