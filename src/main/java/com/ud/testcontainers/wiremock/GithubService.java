package com.ud.testcontainers.wiremock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class GithubService {

    private final String gitHubBaseUrl;
    private final RestClient restClient;

    public GithubService( @Value("${spring.app.github.base-url}") String gitHubBaseUrl,
                          RestClient.Builder githubRestClient) {
        this.gitHubBaseUrl = gitHubBaseUrl;
        this.restClient = githubRestClient.clone().baseUrl(gitHubBaseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public GithubUser getGithubUserProfile(String username) {
        try {
            log.info("Github API BaseUrl: {}", gitHubBaseUrl);

            return restClient.get()
                    .uri("/users/".concat(username))
                    .retrieve()
                    .body(GithubUser.class);
        } catch ( Exception e ) {
            log.error("Fail to fetch github profile");
            throw new GithubServiceException("Fail to fetch github profile for user: " + username);
        }
    }
}
