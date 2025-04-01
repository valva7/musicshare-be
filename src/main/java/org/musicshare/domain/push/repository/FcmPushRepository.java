package org.musicshare.domain.push.repository;

import org.musicshare.domain.member.model.Member;

public interface FcmPushRepository {

    void firebaseTokenSave(Member member, String fcmToken);

    String findReceiverToken(Member receiver);

}
