package org.musicshare.domain.email;


public enum MailTitle {

    SIGNUP_VERIFY("Voice&Melody 회원가입 인증 코드");

    private final String title;

    MailTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
