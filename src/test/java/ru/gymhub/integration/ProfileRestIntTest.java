package ru.gymhub.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import ru.gymhub.dto.GymDto;
import ru.gymhub.dto.request.RegistrationRequest;
import ru.gymhub.dto.request.UpdateGymRequest;

import java.io.UnsupportedEncodingException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ProfileRestIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private RegistrationRequest request;

    @BeforeEach
    void setUp() {
        request =
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
    void getGym_shouldReturnGym() throws Exception {
        // Сначала создаём запись через API
        MvcResult res =
                mockMvc
                        .perform(
                                post("/auth/registration")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk())
                        .andReturn();

        GymDto createdGym = getGymDto(res);
        // Затем получаем её через GET
        mockMvc
                .perform(get("/gym/profile/" + createdGym.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Gym"))
                .andExpect(jsonPath("$.email").value("test@gymhub.ru"));
    }

    @Test
    @WithMockUser
    void updateGym_shouldReturnUpdatedGym() throws Exception {
        // Создаём зал
        MvcResult res =
                mockMvc
                        .perform(
                                post("/auth/registration")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk())
                        .andReturn();

        Long id = getGymDto(res).getId();

        UpdateGymRequest updateRequest =
                UpdateGymRequest.builder()
                        .name("Updated Gym")
                        .description("Updated description")
                        .address("Updated address")
                        .tel("+79991112233")
                        .email("updated@gymhub.ru")
                        .build();

        mockMvc
                .perform(
                        put("/gym/profile/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Gym"))
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.address").value("Updated address"))
                .andExpect(jsonPath("$.tel").value("+79991112233"))
                .andExpect(jsonPath("$.email").value("updated@gymhub.ru"));
    }

    @Test
    @WithMockUser
    void deleteGym_shouldReturnOk() throws Exception {
        // Создаём зал
        MvcResult res =
                mockMvc
                        .perform(
                                post("/auth/registration")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk())
                        .andReturn();

        Long id = getGymDto(res).getId();

        // Удаляем зал
        mockMvc.perform(delete("/gym/profile/" + id)).andExpect(status().isOk());

        // Проверяем, что запись больше не доступна
        mockMvc.perform(get("/gym/profile/" + id)).andExpect(status().isNotFound());
    }

    private GymDto getGymDto(MvcResult res)
            throws UnsupportedEncodingException, JsonProcessingException {
        return objectMapper.readValue(res.getResponse().getContentAsString(), GymDto.class);
    }
}
