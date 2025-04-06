package org.musicshare.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.musicshare.domain.member.repository.FanRepository;
import org.musicshare.global.pricipal.UserAuth;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FanService {

    private final FanRepository fanRepository;

    public boolean getFan(UserAuth user, Long artistId) {
        return fanRepository.existFan(artistId, user.userId());
    }

    public void fanArtist(UserAuth user, Long artistId) {
        if(!user.userId().equals(artistId)) {
            boolean existFan = fanRepository.existFan(artistId, user.userId());

            if(existFan) {
                fanRepository.delete(artistId, user.userId());
            } else {
                fanRepository.save(artistId, user.userId());
            }
        }
    }
}
