package com.ud.testcontainers.integrationtests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class PostControllerTest {

    /**
     * When the @SpringBootTest boots up the whole application, the PostDataLoader.java (implements CommandLineRunner)
     * will read posts.json file and insert 100 records to the database (postgres container defined below).
     */

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldFindAllPosts() {
        List<Post> posts = restTemplate.getForObject("/api/v1/posts", List.class);
        assertThat(posts.size()).isEqualTo(101);
    }

    @Test
    void shouldFindValidPostById() {

        ResponseEntity<Post> response = restTemplate.exchange("/api/v1/posts/1", HttpMethod.GET, null, Post.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().title()).isEqualTo("sunt aut facere repellat provident occaecati excepturi optio reprehenderit");
    }

    @Test
    void shouldThrowNotFoundExceptionWhenInvalidPostId() {
        ResponseEntity<Post> response = restTemplate.exchange("/api/v1/posts/999", HttpMethod.GET, null, Post.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @Rollback
    void shouldCreateNewPostWhenPostIsValid() {
        Post newPost = new Post(101, 1, "101 Title", "101 Body", null);

        ResponseEntity<Post> response = restTemplate.exchange("/api/v1/posts", HttpMethod.POST, new HttpEntity<>(newPost), Post.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(101);
        assertThat(response.getBody().title()).isEqualTo("101 Title");
    }

    @Test
    void shouldNotCreatePostWhenValidationFails() {
        Post newPost = new Post(101, 101, "", "101 Body", null);

        ResponseEntity<Post> response = restTemplate.exchange("/api/v1/posts", HttpMethod.POST, new HttpEntity<>(newPost), Post.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @Rollback
    void shouldUpdatePostByValidId() {
        ResponseEntity<Post> getResponse = restTemplate.exchange("/api/v1/posts/99", HttpMethod.GET, null, Post.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        Post existingPost = getResponse.getBody();
        assertThat(existingPost).isNotNull();
        assertThat(existingPost.id()).isEqualTo(99);

        Post updatePost = new Post(existingPost.id(), existingPost.userId(), "Post 99 Updated Title", "Post 99 Updated Body", null);
        ResponseEntity<Post> updateResponse = restTemplate.exchange("/api/v1/posts/" + existingPost.id(), HttpMethod.PUT, new HttpEntity<>(updatePost), Post.class);

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody()).isNotNull();
        assertThat(updateResponse.getBody().id()).isEqualTo(99);
        assertThat(updateResponse.getBody().title()).isEqualTo("Post 99 Updated Title");
        assertThat(updateResponse.getBody().body()).isEqualTo("Post 99 Updated Body");
    }

    @Test
    @Rollback
    void shouldDeletePostByValidId() {
        ResponseEntity<Void> deleteResponse = restTemplate.exchange("/api/v1/posts/1", HttpMethod.DELETE, null, Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Post> getResponse = restTemplate.exchange("/api/v1/posts/1", HttpMethod.GET, null, Post.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}