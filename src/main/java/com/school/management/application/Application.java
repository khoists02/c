package com.school.management.application;

import com.school.management.application.api.exception.WebExceptionHandler;
import com.school.management.application.support.MyJpaRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("com.school.management")
@ComponentScan("com.school.management")
@EnableJpaRepositories(repositoryFactoryBeanClass = MyJpaRepositoryFactoryBean.class, basePackages = "com.school.management")
@Import(WebExceptionHandler.class)
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class}, scanBasePackages = {"com.school.management"})
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
