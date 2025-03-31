package org.musicshare.domain.push.repository;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.push.entity.FcmTokenEntity;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FcmPushRepositoryImpl implements FcmPushRepository {

    private final JpaFcmPushRepository jpaFcmPushRepository;

    private static final String LIKE_MESSAGE_TEMPLATE = "%s님이 %s님의 음악을 좋아합니다!";
    private static final String MESSAGE_KEY = "message";

    public void firebaseTokenSave(Member member, String fcmToken) {
        FcmTokenEntity fcmTokenEntity = new FcmTokenEntity(member.getId(), fcmToken);
        jpaFcmPushRepository.save(fcmTokenEntity);
    }

    @Override
    public void sendLikeMessage(Member sender, Member receiver) {
        Optional<FcmTokenEntity> tokenEntity = jpaFcmPushRepository.findById(receiver.getId());
        if (tokenEntity.isEmpty()) {
            return;
        }

        FcmTokenEntity token = tokenEntity.get();
        Message message = Message.builder()
            .putData(MESSAGE_KEY, LIKE_MESSAGE_TEMPLATE.formatted(sender.getInfo().getNickname(), receiver.getInfo().getNickname()))
            .setToken(token.getToken())
            .build();
        ApiFuture<String> stringApiFuture = FirebaseMessaging.getInstance().sendAsync(message);
    }

}
