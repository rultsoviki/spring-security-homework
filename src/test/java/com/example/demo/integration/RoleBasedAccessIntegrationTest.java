package com.example.demo.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тесты проверяют, что для проверки ролей в БД должны существовать
 * демо-аккаунты, создаваемые при старте приложения:
 *   - master / masterPass (ROLE_MASTER)
 *   - admin  / adminPass  (ROLE_ADMIN)
 * См. README — раздел «Что нужно сдать».
 */
@AutoConfigureMockMvc
class RoleBasedAccessIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private String loginAs(String username, String password) throws Exception {
        String login = """
                {
                  "username": "%s",
                  "password": "%s"
                }
                """.formatted(username, password);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(login))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString())
                .get("accessToken").asText();
    }

    private String registerAndLoginPlayer(String username) throws Exception {
        String register = """
                {
                  "username": "%s",
                  "password": "playerPass",
                  "email": "%s@dragons.me"
                }
                """.formatted(username, username);
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(register))
                .andExpect(status().isCreated());

        return loginAs(username, "playerPass");
    }

    @Test
    @DisplayName("PLAYER не может создать монстра → 403")
    void player_cannotCreateMonster() throws Exception {
        String token = registerAndLoginPlayer("merry");

        String monster = """
                {
                  "name": "Orc",
                  "hp": 50,
                  "attack": 8,
                  "level": 3
                }
                """;

        mockMvc.perform(post("/api/bestiary")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(monster))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("MASTER может создать монстра → 201")
    void master_canCreateMonster() throws Exception {
        String token = loginAs("master", "masterPass");

        String monster = """
                {
                  "name": "Goblin",
                  "hp": 30,
                  "attack": 5,
                  "level": 2
                }
                """;

        mockMvc.perform(post("/api/bestiary")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(monster))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("MASTER не может банить игроков → 403")
    void master_cannotBanPlayer() throws Exception {
        String token = loginAs("master", "masterPass");

        mockMvc.perform(delete("/api/players/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("ADMIN может банить игроков → 2xx")
    void admin_canBanPlayer() throws Exception {
        String token = loginAs("admin", "adminPass");
        String victim = registerAndLoginPlayer("pippin");

        MvcResult me = mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .get("/api/me")
                        .header("Authorization", "Bearer " + victim))
                .andExpect(status().isOk())
                .andReturn();
        Long victimId = objectMapper.readTree(me.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(delete("/api/players/" + victimId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Без токена POST /api/bestiary → 401, не 403")
    void noToken_onProtectedRoute_returns401() throws Exception {
        String monster = """
                {"name":"X","hp":1,"attack":1,"level":1}
                """;
        mockMvc.perform(post("/api/bestiary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(monster))
                .andExpect(status().isUnauthorized());
    }
}
