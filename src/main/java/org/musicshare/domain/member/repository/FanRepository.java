package org.musicshare.domain.member.repository;

public interface FanRepository{

    boolean existFan(Long artistId, Long fanId);

    void save(Long artistId, Long fanId);

    void delete(Long artistId, Long fanId);

}
