package ru.gymhub.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.gymhub.dto.request.LoginDto;
import ru.gymhub.dto.request.RegistrationRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class AuthTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private RegistrationRequest registrationRequest;

    @BeforeEach
    void setUp() {
        registrationRequest =
                RegistrationRequest.builder()
                        .name("Test Gym")
                        .description("Test description")
                        .address("Test address")
                        .tel("+79991234567")
                        .email("test@gymhub.ru")
                        .password("Pass@12Hash")
                        .build();
    }

    @Test
    @WithMockUser
    void createGym_withValidData_shouldReturnGym() throws Exception {
        mockMvc
                .perform(
                        post("/auth/registration")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test Gym"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.address").value("Test address"))
                .andExpect(jsonPath("$.tel").value("+79991234567"))
                .andExpect(jsonPath("$.email").value("test@gymhub.ru"));
    }

    @Test
    @WithMockUser
    void createGym_withInvalidData_shouldThrowValidationException() throws Exception {
        registrationRequest.setName(" ");
        mockMvc
                .perform(
                        post("/auth/registration")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void createGym_whenEmailIsExist_shouldThrowValidationException() throws Exception {

        mockMvc
                .perform(
                        post("/auth/registration")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk());

        mockMvc
                .perform(
                        post("/auth/registration")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isConflict());
    }


    @Test
    @WithMockUser
    void login_success_shouldReturnGym() throws Exception {

        mockMvc
                .perform(
                        post("/auth/registration")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk());

        LoginDto login =
                LoginDto.builder().email(registrationRequest.getEmail()).password(registrationRequest.getPassword()).build();

        mockMvc
                .perform(
                        put("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Test Gym"))
                .andExpect(jsonPath("$.description").value("Test description"))
                .andExpect(jsonPath("$.address").value("Test address"))
                .andExpect(jsonPath("$.tel").value("+79991234567"))
                .andExpect(jsonPath("$.email").value("test@gymhub.ru"));
    }

    @Test
    @WithMockUser
    void login_whenEmailNotFound_shouldThrowGymNotFound() throws Exception {

        mockMvc
                .perform(
                        post("/auth/registration")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk());

        LoginDto login =
                LoginDto.builder().email("myWrongEmail@ya.su").password(registrationRequest.getPassword()).build();

        mockMvc
                .perform(
                        put("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void login_whenWrongPassword_shouldThrowWrongPassword() throws Exception {

        mockMvc
                .perform(
                        post("/auth/registration")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk());

        LoginDto login =
                LoginDto.builder().email(registrationRequest.getEmail()).password("Mywrong1@1password").build();

        mockMvc
                .perform(
                        put("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isBadRequest());
    }


}
