package com.ud.testcontainers.integrationtests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJdbcTest
class PostRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    PostRepository postRepository;

    // test if the postgres container is up and running.
    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @BeforeEach
    void setUp() {
        List<Post> posts = List.of(new Post(1, 1, "Hello, Udith!", "Integration Test database layer in postgres", null));
        postRepository.saveAll(posts);
    }

    @Test
    void shouldReturnPostByTitle() {
        Post post = postRepository.findByTitle("Hello, Udith!");
        System.out.println("Returned post: " + post);
        assertThat(post).isNotNull();
        assertThat(post.title()).isEqualTo("Hello, Udith!");
        assertThat(post.body()).isEqualTo("Integration Test database layer in postgres");
    }

}