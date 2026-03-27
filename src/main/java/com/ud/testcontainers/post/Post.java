package com.ud.testcontainers.post;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;


public record Post(
        @Id
        Integer id,
        @NotNull
        @Positive
        Integer userId,
        @NotEmpty
        String title,
        String body,
        @Version
        Integer version)
{}
