package com.sk.skala.myapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 애플리케이션 진입점 클래스
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.sk.skala.myapp.repository")
public class MyappApplication {
    /**
     * 메인 메서드
     * @param args 명령행 인자
     */
    public static void main(String[] args) {
        SpringApplication.run(MyappApplication.class, args);
    }
}