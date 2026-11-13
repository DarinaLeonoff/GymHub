package ru.gymhub.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.gymhub.dto.GymDto;
import ru.gymhub.dto.request.LoginDto;
import ru.gymhub.dto.request.RegistrationRequest;
import ru.gymhub.exceptions.exceptions.EmailAlreadyRegisteredException;
import ru.gymhub.exceptions.exceptions.WrongPasswordException;
import ru.gymhub.service.GymAuthService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockitoBean
    private GymAuthService service;

    @Test
    void createGymInfo_shouldReturnGymDto() throws Exception {

        RegistrationRequest request =
                RegistrationRequest.builder()
                        .name("GymHub")
                        .description("Modern fitness center")
                        .address("Moscow, Lenina 10")
                        .tel("+79991234567")
                        .email("gymhub@example.com")
                        .password("paSsword@12")
                        .build();

        GymDto response =
                GymDto.builder()
                        .id(1L)
                        .name("GymHub")
                        .description("Modern fitness center")
                        .address("Moscow, Lenina 10")
                        .tel("+79991234567")
                        .email("gymhub@example.com")
                        .build();

        when(service.createGym(any(RegistrationRequest.class))).thenReturn(response);

        mockMvc
                .perform(
                        post("/auth/registration")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("GymHub"))
                .andExpect(jsonPath("$.description").value("Modern fitness center"))
                .andExpect(jsonPath("$.address").value("Moscow, Lenina 10"))
                .andExpect(jsonPath("$.tel").value("+79991234567"))
                .andExpect(jsonPath("$.email").value("gymhub@example.com"));

        verify(service).createGym(any(RegistrationRequest.class));
    }

    @Test
    void createGym_withInvalidData_shouldReturn400() throws Exception {

        RegistrationRequest request =
                RegistrationRequest.builder()
                        .name("")
                        .description("Description")
                        .address("Moscow, Lenina 10")
                        .tel("+79991234567")
                        .email("gymhub@example.com")
                        .password("passSword@12")
                        .build();

        mockMvc
                .perform(
                        post("/auth/registration")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void createGym_withExistingEmail_shouldReturn409() throws Exception {

        RegistrationRequest request =
                RegistrationRequest.builder()
                        .name("Name")
                        .description("Description")
                        .address("Moscow, Lenina 10")
                        .tel("+79991234567")
                        .email("gymhub@example.com")
                        .password("passSword@12")
                        .build();

        when(service.createGym(any(RegistrationRequest.class))).thenThrow(new EmailAlreadyRegisteredException());

        mockMvc
                .perform(
                        post("/auth/registration")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void login_shouldReturnGymDto() throws Exception {

        LoginDto request =
                LoginDto.builder()
                        .email("email@email.ru")
                        .password("pass@Wo12rd")
                        .build();

        GymDto response =
                GymDto.builder()
                        .id(1L)
                        .name("GymHub")
                        .description("Modern fitness center")
                        .address("Moscow, Lenina 10")
                        .tel("+79991234567")
                        .email("gymhub@example.com")
                        .build();

        when(service.login(any(LoginDto.class))).thenReturn(response);

        mockMvc
                .perform(
                        put("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(service).login(any(LoginDto.class));
    }

    @Test
    void login_shouldReturn400() throws Exception {

        LoginDto request =
                LoginDto.builder()
                        .email("email@email.ru")
                        .password("pass@Wo12rd")
                        .build();

        when(service.login(any(LoginDto.class))).thenThrow(new WrongPasswordException(request.getEmail()));

        mockMvc
                .perform(
                        put("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

}
