package com.ud.testcontainers.wiremock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/github")
public class GithubController {

    private final GithubService gitHubService;

    public GithubController(GithubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<GithubUser> getGitHubUserProfile(@PathVariable String username) {
        GithubUser gitHubUserProfile = gitHubService.getGithubUserProfile(username);
        log.info("Github User Profile found for : {}", gitHubUserProfile.getName());
        return ResponseEntity.ok(gitHubUserProfile);
    }

}
