package com.example.demo.integration;

import org.junit.jupiter.api.DisplayName;
import org.springframework.test.annotation.DirtiesContext;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ProtectedRoutesIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private String registerAndLogin(String username) throws Exception {
        String register = """
                {
                  "username": "%s",
                  "password": "secretPass123",
                  "email": "%s@dragons.me"
                }
                """.formatted(username, username);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(register))
                .andExpect(status().isCreated());

        String login = """
                {
                  "username": "%s",
                  "password": "secretPass123"
                }
                """.formatted(username);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(login))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString())
                .get("accessToken").asText();
    }

    @Test
    @DisplayName("GET /api/me без токена → 401")
    void me_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/api/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/me с валидным токеном → 200 и username")
    void me_withValidToken_returns200() throws Exception {
        String token = registerAndLogin("bilbo");

        mockMvc.perform(get("/api/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("bilbo"));
    }

    @Test
    @DisplayName("GET /api/me с битым токеном → 401")
    void me_withInvalidToken_returns401() throws Exception {
        mockMvc.perform(get("/api/me")
                        .header("Authorization", "Bearer not-a-real-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/inventory без токена → 401, с токеном → 200")
    void inventory_requiresAuth() throws Exception {
        mockMvc.perform(get("/api/inventory"))
                .andExpect(status().isUnauthorized());

        String token = registerAndLogin("gimli");
        mockMvc.perform(get("/api/inventory")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
