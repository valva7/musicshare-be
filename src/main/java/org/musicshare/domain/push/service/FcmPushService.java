package org.musicshare.domain.push.service;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.musicshare.domain.member.model.Member;
import org.musicshare.domain.push.repository.FcmPushRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmPushService {

    private static final String LIKE_MESSAGE_TEMPLATE = "%s님이 %s님의 음악을 좋아합니다!";
    private static final String MESSAGE_KEY = "message";

    private final FcmPushRepository fcmPushRepository;

    public void sendLikeMessage(Member sender, Member receiver) {
        String receiverToken = fcmPushRepository.findReceiverToken(receiver);

        if(!receiverToken.isEmpty()) {
            Message message = Message.builder()
                .putData(MESSAGE_KEY, LIKE_MESSAGE_TEMPLATE.formatted(sender.getInfo().getNickname(), receiver.getInfo().getNickname()))
                .setToken(receiverToken)
                .build();
            FirebaseMessaging.getInstance().sendAsync(message);
        }
    }

}
