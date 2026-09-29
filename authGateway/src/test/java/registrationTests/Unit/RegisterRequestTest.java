package registrationTests.Unit;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.gymhub.authorisation.dto.RegisterRequest;
import ru.gymhub.authorisation.entity.User;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class RegisterRequestTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "", " ", "email@", "@ya.ru"})
    public void invalidEmailTest(String email) {
        RegisterRequest request =
                RegisterRequest.builder().email(email).password("pass1@Word").build();

        Set<ConstraintViolation<RegisterRequest>> violations =
                validator.validate(request);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "pas", "password", "pass@word", "pass1@word"})
    public void invalidPasswordTest(String password) {
        RegisterRequest request = RegisterRequest.builder().email("email@ya.ru").password(password).build();

        Set<ConstraintViolation<RegisterRequest>> violations =
                validator.validate(request);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    public void validRegisterRequestTest() {
        RegisterRequest request = RegisterRequest.builder().email("email@ya.ru").password("pass1@Word").accType(User.AccountType.CLIENT).role(User.RoleType.CLIENT).build();

        Set<ConstraintViolation<RegisterRequest>> violations =
                validator.validate(request);

        assertThat(violations).isEmpty();

    }
}
