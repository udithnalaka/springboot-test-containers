package com.ud.testcontainers.wiremock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GithubControllerTest {

    @Container
    //@ServiceConnection
    static WireMockContainer wiremock = new WireMockContainer("wiremock/wiremock:3.5.2");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.app.github.base-url", wiremock::getBaseUrl);
    }

    @Autowired
    TestRestTemplate restTemplate;

    @BeforeEach
    void setup() {
        configureFor(
                wiremock.getHost(),
                wiremock.getMappedPort(8081)
        );
    }

    @Test
    void shouldReturnGitHubProfileForValidUser() {

        String username = "udith";

        stubFor(get(urlEqualTo("/api/v1/github/users/.*"))
                .willReturn(
                        aResponse()
                                .withHeader("Accept", "application/json")
                                .withBody("""
                                       {
                                         "id": 161434629,
                                         "login": "uditha007",
                                         "name": udith,
                                         "company": AWS,
                                         "blog": "",
                                         "location": null,
                                         "email": "u@u.com",
                                         "bio": "Java developer",
                                         "public_repos": "20"
                                       }
                                       """.formatted(username))));

        GithubUser user = restTemplate.getForObject("/api/v1/github/users/"+ username, GithubUser.class);
        assertThat(user).isNotNull();
    }
}