package org.musicshare.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.musicshare.domain.member.model.Fan;
import org.musicshare.domain.member.model.entity.FanEntity;
import org.musicshare.domain.member.model.entity.FanIdEntity;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FanRepositoryImpl implements FanRepository {

    private final JpaFanRepository jpaFanRepository;

    public boolean existFan(Long artistId, Long fanId) {
        return jpaFanRepository.existsById(new FanIdEntity(artistId, fanId));
    }

    public void save(Fan fan) {
        jpaFanRepository.save(new FanEntity(fan));
    }

    public void delete(Fan fan) {
        jpaFanRepository.delete(new FanEntity(fan));
    }

}
