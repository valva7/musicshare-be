package org.musicshare.test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import org.musicshare.domain.auth.dto.req.LoginReq;

public class LoginReqValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 유효한_로그인_요청이면_에러가_없다() {
        LoginReq req = new LoginReq("test@example.com", "password123", null);

        Set<ConstraintViolation<LoginReq>> violations = validator.validate(req);

        assertThat(violations).isEmpty();
    }

    @Test
    void 이메일이_비어있으면_에러() {
        LoginReq req = new LoginReq("", "password123", null);

        Set<ConstraintViolation<LoginReq>> violations = validator.validate(req);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void 이메일_형식이_아니면_에러() {
        LoginReq req = new LoginReq("invalid-email", "password123", null);

        Set<ConstraintViolation<LoginReq>> violations = validator.validate(req);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void 비밀번호가_너무_짧으면_에러() {
        LoginReq req = new LoginReq("test@example.com", "123", null);

        Set<ConstraintViolation<LoginReq>> violations = validator.validate(req);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }
}