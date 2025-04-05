package org.musicshare;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.musicshare.domain.auth.dto.req.LoginReq;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MusicShareApplicationTests {

    private Validator validator;

    @Test
    void contextLoads() {
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

}
