package ru.gymhub.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.gymhub.dto.GymDto;
import ru.gymhub.dto.request.UpdateGymRequest;
import ru.gymhub.service.GymService;

@WebMvcTest(GymProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
class GymProfileControllerWebTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private GymService service;

  // ---------- UPDATE ----------

  @Test
  void updateGymInfo_shouldReturnUpdatedGymDto() throws Exception {

    UpdateGymRequest request =
        UpdateGymRequest.builder()
            .name("GymHub Updated")
            .description("Updated description")
            .address("Moscow, Lenina 20")
            .tel("+79991234567")
            .email("updated@example.com")
            .build();

    GymDto response =
        GymDto.builder()
            .id(1L)
            .name("GymHub Updated")
            .description("Updated description")
            .address("Moscow, Lenina 20")
            .tel("+79991234567")
            .email("updated@example.com")
            .build();

    when(service.updateGymInfo(eq(1L), any(UpdateGymRequest.class))).thenReturn(response);

    mockMvc
        .perform(
            put("/gym/profile/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("GymHub Updated"))
        .andExpect(jsonPath("$.description").value("Updated description"))
        .andExpect(jsonPath("$.address").value("Moscow, Lenina 20"))
        .andExpect(jsonPath("$.email").value("updated@example.com"));

    verify(service).updateGymInfo(eq(1L), any(UpdateGymRequest.class));
  }

  @Test
  void updateGymInfo_withInvalidData_shouldReturn400() throws Exception {

    UpdateGymRequest request =
        UpdateGymRequest.builder().name("GymHub").email("invalid-email").build();

    mockMvc
        .perform(
            put("/gym/profile/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());

    verifyNoInteractions(service);
  }

  @Test
  void updateGymInfo_withValidTel_shouldReturn200() throws Exception {

    UpdateGymRequest request = UpdateGymRequest.builder().name("GymHub").tel("89999999999").build();

    mockMvc
        .perform(
            put("/gym/profile/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  void updateGymInfo_withInvalidTel_shouldReturn400() throws Exception {

    UpdateGymRequest request =
        UpdateGymRequest.builder().name("GymHub").tel("+10909999999").build();

    mockMvc
        .perform(
            put("/gym/profile/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());

    verifyNoInteractions(service);
  }

  @Test
  void updateGymInfo_withNullTel_shouldReturn200() throws Exception {

    UpdateGymRequest request = UpdateGymRequest.builder().name("GymHub").tel(" ").build();

    mockMvc
        .perform(
            put("/gym/profile/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  // ---------- GET ----------

  @Test
  void getGymInfo_shouldReturnGymDto() throws Exception {

    GymDto response =
        GymDto.builder()
            .id(1L)
            .name("GymHub")
            .description("Modern fitness center")
            .address("Moscow, Lenina 10")
            .tel("+79991234567")
            .email("gymhub@example.com")
            .build();

    when(service.getGymInfo(1L)).thenReturn(response);

    mockMvc
        .perform(get("/gym/profile/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("GymHub"))
        .andExpect(jsonPath("$.address").value("Moscow, Lenina 10"));

    verify(service).getGymInfo(1L);
  }

  // ---------- DELETE ----------

  @Test
  void deleteGym_shouldReturn200() throws Exception {

    doNothing().when(service).deleteGym(1L);

    mockMvc.perform(delete("/gym/profile/1")).andExpect(status().isOk());

    verify(service).deleteGym(1L);
  }
}
