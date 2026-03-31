package com.ud.testcontainers.wiremock;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Builder
@AllArgsConstructor
public class GithubUser {

    private Long id;
    private String login;
    private String name;
    private String company;
    private String blog;
    private String location;
    private String email;
    private String bio;
    @JsonProperty("public_repos")
    private String publicRepos;
}
