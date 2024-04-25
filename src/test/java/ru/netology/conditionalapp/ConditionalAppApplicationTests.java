package ru.netology.conditionalapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConditionalAppApplicationTests {
    @Autowired
    TestRestTemplate restTemplate;


    @Container
    GenericContainer<?> devapp = new GenericContainer<>("devapp:latest")
            .withExposedPorts(8080);

    @Container
    GenericContainer<?> prodapp = new GenericContainer<>("prodapp:latest")
            .withExposedPorts(8081);

    @BeforeEach
    void setUp() {
        devapp.start();
        prodapp.start();
    }

    @Test
    void contextLoads() {
        // Получение ответа от наших приложенек
        ResponseEntity<String> devResponseEntity = restTemplate.getForEntity("http://localhost:" +
                devapp.getMappedPort(8080) + "/profile", String.class);
        ResponseEntity<String> prodResponseEntity = restTemplate.getForEntity("http://localhost:" +
                prodapp.getMappedPort(8081) + "/profile", String.class);

        // Ожидаемые значения
        String devExpect = "Current profile is Dev";
        String prodExpect = "Current profile is production";

        assertEquals(devExpect, devResponseEntity.getBody());
        assertEquals(prodExpect, prodResponseEntity.getBody());
    }

}
