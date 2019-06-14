package com.tiger.jpa_json_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JpaJsonDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaJsonDemoApplication.class, args);
    }

}
