package org.musicshare.global.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

    private final String host;
    private final String username;
    private final String password;
    private final int port;
    private final boolean auth;
    private final boolean debug;
    private final int connectionTimeout;
    private final boolean startTlsEnable;

    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_DEBUG = "mail.smtp.debug";
    private static final String MAIL_CONNECTION_TIMEOUT = "mail.smtp.connectiontimeout";
    private static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";

    public MailConfig(
        @Value("${spring.mail.host}") String host,
        @Value("${spring.mail.username}") String username,
        @Value("${spring.mail.password}") String password,
        @Value("${spring.mail.port}") int port,
        @Value("${spring.mail.properties.mail.smtp.auth}") boolean auth,
        @Value("${spring.mail.properties.mail.smtp.debug}") boolean debug,
        @Value("${spring.mail.properties.mail.smtp.timeout}") int connectionTimeout,
        @Value("${spring.mail.properties.mail.smtp.starttls.enable}") boolean startTlsEnable
    ) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.port = port;
        this.auth = auth;
        this.debug = debug;
        this.connectionTimeout = connectionTimeout;
        this.startTlsEnable = startTlsEnable;
    }

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        javaMailSender.setPort(port);

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.put(MAIL_SMTP_AUTH, auth);
        properties.put(MAIL_DEBUG, debug);
        properties.put(MAIL_CONNECTION_TIMEOUT, connectionTimeout);
        properties.put(MAIL_SMTP_STARTTLS_ENABLE, startTlsEnable);

        javaMailSender.setJavaMailProperties(properties);
        javaMailSender.setDefaultEncoding("UTF-8");

        return javaMailSender;
    }
}
