package com.example.demo;

import com.example.demo.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DemoApplicationTests extends AbstractIntegrationTest {

    @Test
    void contextLoads() {
    }

}
