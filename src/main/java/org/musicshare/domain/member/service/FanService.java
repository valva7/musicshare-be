package org.musicshare.domain.member.service;

import org.musicshare.domain.member.repository.FanRepository;
import org.musicshare.global.pricipal.UserAuth;
import org.springframework.stereotype.Service;

@Service
public class FanService {

    private final FanRepository fanRepository;

    public FanService(FanRepository fanRepository) {
        this.fanRepository = fanRepository;
    }

    /**
     * 팬 여부 확인
     * @param user
     * @param artistId
     * @return
     */
    public boolean getFan(UserAuth user, Long artistId) {
        return fanRepository.existFan(artistId, user.userId());
    }

    /**
     * 팬 등록 / 해제
     * @param user
     * @param artistId
     */
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
