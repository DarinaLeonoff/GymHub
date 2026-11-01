package ru.gymhub.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.gymhub.dto.GymDto;
import ru.gymhub.dto.UpdateGymRequest;
import ru.gymhub.service.GymService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GymProfileController.class)
public class GymProfileControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GymService gymService;

    @Test
    void testPut() throws Exception {
        String jsonPayload = "{\"name\": \"new name\"}";
        UpdateGymRequest req = UpdateGymRequest.builder().name("new name").build();

        when(gymService.updateGymInfo(1L, req)).thenReturn(new GymDto());

        mockMvc.perform(put("/gym/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk());
    }
}

