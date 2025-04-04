package org.musicshare.domain.email.service;

import jakarta.mail.internet.MimeMessage;
import java.time.Duration;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.musicshare.domain.email.util.MailTemplate;
import org.musicshare.domain.member.repository.JpaMemberRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final JpaMemberRepository jpaMemberRepository;

    private final StringRedisTemplate redisTemplate;

    public void sendSignUpVerifyCode(String email) {
        // 가입 이메일 존재 여부 확인
        boolean exist = jpaMemberRepository.existsByEmail(email);
        if (exist) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        // 인증 코드 생성
        String verifyCode = createSignUpVerifyCode(email);

        // 인증 메일 전송
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setSubject("Voice&Melody 회원가입 인증 코드"); // 메일 제목
            mimeMessageHelper.setText(MailTemplate.getSignUpVerifyTemplate(verifyCode), true);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String createSignUpVerifyCode(String email) {
        String savedCode = redisTemplate.opsForValue().get(email);
        // 이메일 인증 코드가 이미 존재하는 경우 삭제
        if (savedCode != null) {
            redisTemplate.delete(email);
        }
        // 인증 코드 생성
        Random random = new Random();
        String verifyCode = String.format("%06d", random.nextInt(1000000));
        // 인증 코드 Redis에 저장 (5분)
        redisTemplate.opsForValue().set(email, verifyCode, Duration.ofMinutes(5));

        return verifyCode;
    }

}
