package bth.ui.controller;

import bth.ui.config.SecurityConfig;
import bth.ui.service.RedisWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
@Import(SecurityConfig.class)
@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RedisWrapper redisWrapper;

    @Test
    @WithMockUser
    void testSaveNotificationToken() throws Exception {
        // Given
        String token = "test-token";
        Map<String, String> requestData = Map.of("token", token);

        // When
        mockMvc.perform(post("/notification/token")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(requestData)))
                .andExpect(status().isOk())
                .andExpect(content().string("Token saved"));

        // Then
        verify(redisWrapper, times(1)).set("notification_token", token);
    }

    @Test
    @WithMockUser
    void testSaveNotificationTokenWhenNoTokenKeyExists() throws Exception {
        // Given
        String token = "test-token";
        Map<String, String> requestData = Map.of("not_token_key", token);

        // When
        mockMvc.perform(post("/notification/token")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsBytes(requestData)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Token is required"));

        // Then
        verify(redisWrapper, never()).set("notification_token", token);
    }
}