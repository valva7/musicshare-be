package org.musicshare.domain.push.repository;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.push.entity.FcmTokenEntity;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class FcmPushRepositoryImpl implements FcmPushRepository {

    private final JpaFcmPushRepository jpaFcmPushRepository;

    public FcmPushRepositoryImpl(JpaFcmPushRepository jpaFcmPushRepository) {
        this.jpaFcmPushRepository = jpaFcmPushRepository;
    }


    public void firebaseTokenSave(Member member, String fcmToken) {
        FcmTokenEntity fcmTokenEntity = new FcmTokenEntity(member.getId(), fcmToken);
        jpaFcmPushRepository.save(fcmTokenEntity);
    }

    @Override
    public String findReceiverToken(Member receiver) {
        Optional<FcmTokenEntity> tokenEntity = jpaFcmPushRepository.findById(receiver.getId());
        if (tokenEntity.isEmpty()) {
            return "";
        } else {
            return tokenEntity.get().getToken();
        }
    }

}
