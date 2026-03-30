package com.ud.testcontainers.integrationtests;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/posts")
public class PostController {

    private final static Logger LOGGER = LoggerFactory.getLogger(PostController.class);
    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping
    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable Integer id) {
        return postRepository.findById(id).orElseThrow(PostNotFoundException::new);
    }

    @PostMapping("")
    public Post createPost(@RequestBody @Valid Post post) {
        return postRepository.save(post);
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Integer id, @RequestBody Post post) {
        Optional<Post> existingPost = postRepository.findById(id);
        if (existingPost.isPresent()) {
            Post updatedPost = new Post(id, existingPost.get().userId(),
                    post.title(), post.body(), existingPost.get().version());

            return postRepository.save(updatedPost);
        }  else {
            throw new PostNotFoundException();
        }
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable Integer id) {
        postRepository.deleteById(id);
    }

}
