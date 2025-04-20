package org.musicshare.domain.email.service;

import jakarta.mail.MessagingException;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import jakarta.mail.internet.MimeMessage;
import java.time.Duration;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.musicshare.domain.email.MailTitle;
import org.musicshare.domain.email.util.MailContent;
import org.musicshare.domain.member.repository.JpaMemberRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    private final StringRedisTemplate redisTemplate;

    private final JavaMailSender javaMailSender;
    private final JpaMemberRepository jpaMemberRepository;

    public EmailService(StringRedisTemplate redisTemplate, JavaMailSender javaMailSender, JpaMemberRepository jpaMemberRepository) {
        this.redisTemplate = redisTemplate;
        this.javaMailSender = javaMailSender;
        this.jpaMemberRepository = jpaMemberRepository;
    }


    /**
     * 회원가입 이메일 인증 코드 전송
     * @param email
     */
    public void sendSignUpVerifyCode(String email) {
        // 가입 이메일 존재 여부 확인
        boolean exist = jpaMemberRepository.existsByEmail(email);
        if (exist) throw new IllegalArgumentException("이미 가입된 이메일입니다.");

        String title = MailTitle.SIGNUP_VERIFY.getTitle();
        // 인증 코드 생성
        String verifyCode = createSignUpVerifyCode(email);
        String mailTemplate = MailContent.signUpVerify(verifyCode);

        sendEmail(email, title, mailTemplate);
    }

    private void sendEmail(String receiver, String title, String signUpVerifyTemplate) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(receiver); // 메일 수신자
            mimeMessageHelper.setSubject(title); // 메일 제목
            mimeMessageHelper.setText(signUpVerifyTemplate, true); // 메일 내용
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("메일 메시지 생성 오류", e);
            throw new MailSendException("메일 메시지 생성에 실패했습니다.");
        } catch (MailAuthenticationException e) {
            log.error("메일 인증 오류", e);
            throw new RuntimeException("메일 인증에 실패했습니다.");
        } catch (MailException e) {
            log.error("메일 전송 오류", e);
            throw new RuntimeException("메일 전송에 실패했습니다.");
        } catch (Exception e) {
            log.error("알 수 없는 메일 전송 오류", e);
            throw new RuntimeException("알 수 없는 오류가 발생했습니다.");
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
