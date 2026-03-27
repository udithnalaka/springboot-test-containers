package com.ud.testcontainers;

public class TestSpringApplication {

	public static void main(String[] args) {
		org.springframework.boot.SpringApplication.from(SpringApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
