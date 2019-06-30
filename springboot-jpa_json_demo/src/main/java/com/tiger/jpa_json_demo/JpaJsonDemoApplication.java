package com.tiger.jpa_json_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling//开启定时任务支持
public class JpaJsonDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaJsonDemoApplication.class, args);
    }

}
