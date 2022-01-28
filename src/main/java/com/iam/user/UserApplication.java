package com.iam.user;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


/**
 * @author anaghesh Muruli
 */
@SpringBootApplication
@EnableJpaAuditing
@OpenAPIDefinition(info = @Info(title = "Users API", version = "1.0", description = "User Management APIs which provide CRUD operations"))
public class UserApplication {
	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

}
