package userTests.Integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.gymhub.gymhub.GymhubApplication;
import ru.gymhub.gymhub.user.UserRepository;
import ru.gymhub.gymhub.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = GymhubApplication.class)
@AutoConfigureMockMvc
class UserController {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setFirstName("Daria");
        user.setLastName("Leonoff");
        user.setPasswordHash(passwordEncoder.encode("Password1!"));
        user.setActive(true);
        user.setBirthDay(LocalDate.now().minusYears(20L));
        user.setCity("City");
        user.setPhone("77777777777");
        user.setActive(true);
        user.setCreated(LocalDateTime.now());

        userRepository.save(user);
    }

    @Test
    @WithMockUser(username = "test@mail.com")
    void getMeShouldReturn200() throws Exception {

        mockMvc.perform(get("users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@mail.com"));
    }
}
