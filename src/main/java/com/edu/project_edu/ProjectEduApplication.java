package com.edu.project_edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ProjectEduApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectEduApplication.class, args);
	}

}
