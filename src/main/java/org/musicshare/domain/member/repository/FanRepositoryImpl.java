package org.musicshare.domain.member.repository;

import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.member.model.entity.FanEntity;
import org.musicshare.domain.member.model.entity.FanIdEntity;
import org.springframework.stereotype.Repository;

@Repository
public class FanRepositoryImpl implements FanRepository {

    private final JpaFanRepository jpaFanRepository;
    private final MemberRepository memberRepository;

    public FanRepositoryImpl(JpaFanRepository jpaFanRepository, MemberRepository memberRepository) {
        this.jpaFanRepository = jpaFanRepository;
        this.memberRepository = memberRepository;
    }

    public boolean existFan(Long artistId, Long fanId) {
        return jpaFanRepository.existsById(new FanIdEntity(artistId, fanId));
    }

    public void save(Long artistId, Long fanId) {
        Member artist = memberRepository.findMemberById(artistId);
        Member fan = memberRepository.findMemberById(fanId);
        jpaFanRepository.save(new FanEntity(artist, fan));
    }

    public void delete(Long artistId, Long fanId) {
        Member artistMember = memberRepository.findMemberById(artistId);
        Member fanMember = memberRepository.findMemberById(fanId);
        jpaFanRepository.delete(new FanEntity(artistMember, fanMember));
    }

}
