package registrationTests.Unit;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.gymhub.gymhub.authorisation.dto.RegisterRequest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    public void invalidEmailTest (String email){
        RegisterRequest request =
                RegisterRequest.builder().email(email).firstName("Name").password("pass1@Word").birthDay(LocalDate.now().minusYears(20L)).city("my city").phone("77777777777").build();

        Set<ConstraintViolation<RegisterRequest>> violations =
                validator.validate(request);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void invalidNameTest (String name){
        RegisterRequest request = RegisterRequest.builder().email("email@ya.ru").firstName(name).password("pass1@Word").birthDay(LocalDate.now().minusYears(20L)).city("my city").phone("77777777777").build();

        Set<ConstraintViolation<RegisterRequest>> violations =
                validator.validate(request);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "pas", "password", "pass@word", "pass1@word"})
    public void invalidPasswordTest (String password){
        RegisterRequest request = RegisterRequest.builder().email("email@ya.ru").firstName("name").password(password).birthDay(LocalDate.now().minusYears(20L)).city("my city").phone("77777777777").build();

        Set<ConstraintViolation<RegisterRequest>> violations =
                validator.validate(request);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2024-07-25",
            "2024-07-26",
            "2024-08-01"
    })
    void validBirthday(String birthday) {
        RegisterRequest request = RegisterRequest.builder()
                .email("email@ya.ru")
                .firstName("name")
                .password("pass1@Word")
                .birthDay(LocalDate.parse(birthday))
                .city("my city")
                .phone("77777777777")
                .build();

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations)
                .noneMatch(v -> v.getPropertyPath().toString().equals("birthDay"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "passwordmepasswordmepasswordmepasswordmepasswordmepasswordmepasswordmepasswordmepasswordmepasswordmepasswordme"})
    public void invalidCityTest (String city){
        RegisterRequest request = RegisterRequest.builder()
                .email("email@ya.ru")
                .firstName("name")
                .password("pass1@Word")
                .birthDay(LocalDate.now().minusYears(20L))
                .city(city)
                .phone("77777777777")
                .build();

        Set<ConstraintViolation<RegisterRequest>> violations =
                validator.validate(request);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("city"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "222", "3453241545635656"})
    public void invalidPhoneNumberTest (String number){
        RegisterRequest request = RegisterRequest.builder()
                .email("email@ya.ru")
                .firstName("name")
                .password("pass1@Word")
                .birthDay(LocalDate.now().minusYears(20L))
                .city("my city")
                .phone(number)
                .build();

        Set<ConstraintViolation<RegisterRequest>> violations =
                validator.validate(request);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("phone"));
    }

    @Test
    public void validRegisterRequestTest(){
            RegisterRequest request = RegisterRequest.builder().email("email@ya.ru").firstName("name").password("pass1@Word").birthDay(LocalDate.now().minusYears(20L)).city("my city").phone("77777777777").build();

            Set<ConstraintViolation<RegisterRequest>> violations =
                    validator.validate(request);

            assertThat(violations).isEmpty();

    }
}
