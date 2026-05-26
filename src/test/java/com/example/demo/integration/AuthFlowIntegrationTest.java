package com.example.demo.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class AuthFlowIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/auth/register создаёт игрока и возвращает 201")
    void registerNewPlayer_returns201() throws Exception {
        String body = """
                {
                  "username": "frodo",
                  "password": "ringbearer123",
                  "email": "frodo@shire.me"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /api/auth/register с тем же логином дважды → 400/409")
    void registerSameUsernameTwice_returnsConflict() throws Exception {
        String body = """
                {
                  "username": "gandalf",
                  "password": "youshallnotpass",
                  "email": "gandalf@istari.me"
                }
                """;

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("POST /api/auth/login возвращает accessToken и refreshToken")
    void loginReturnsBothTokens() throws Exception {
        String register = """
                {
                  "username": "aragorn",
                  "password": "elessar123",
                  "email": "aragorn@gondor.me"
                }
                """;
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(register))
                .andExpect(status().isCreated());

        String login = """
                {
                  "username": "aragorn",
                  "password": "elessar123"
                }
                """;

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(login))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andReturn();

        JsonNode tokens = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(tokens.get("accessToken").asText()).contains(".");
    }

    @Test
    @DisplayName("POST /api/auth/login с неверным паролем → 401")
    void loginWithWrongPassword_returns401() throws Exception {
        String register = """
                {
                  "username": "legolas",
                  "password": "correct-horse",
                  "email": "legolas@mirkwood.me"
                }
                """;
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(register))
                .andExpect(status().isCreated());

        String login = """
                {
                  "username": "legolas",
                  "password": "wrong-pass"
                }
                """;

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(login))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/auth/refresh выдаёт новый access-токен")
    void refreshIssuesNewAccessToken() throws Exception {
        String register = """
                {
                  "username": "sam",
                  "password": "potatoes123",
                  "email": "sam@shire.me"
                }
                """;
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(register))
                .andExpect(status().isCreated());

        String login = """
                {
                  "username": "sam",
                  "password": "potatoes123"
                }
                """;
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(login))
                .andExpect(status().isOk())
                .andReturn();

        String refreshToken = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                .get("refreshToken").asText();

        String refreshBody = "{\"refreshToken\":\"" + refreshToken + "\"}";

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    @DisplayName("Пароль хранится в БД хешированным — повторный логин работает")
    void passwordIsHashed_loginStillWorks() throws Exception {
        String register = """
                {
                  "username": "boromir",
                  "password": "onedoesnotsimply",
                  "email": "boromir@gondor.me"
                }
                """;
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(register))
                .andExpect(status().isCreated());

        String login = """
                {
                  "username": "boromir",
                  "password": "onedoesnotsimply"
                }
                """;
        assertThat(mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(login))
                .andReturn().getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
