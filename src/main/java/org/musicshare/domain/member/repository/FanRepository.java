package org.musicshare.domain.member.repository;

import org.musicshare.domain.member.model.Fan;

public interface FanRepository{

    boolean existFan(Long artistId, Long fanId);

    void save(Fan fan);

    void delete(Fan fan);

}
