package com.sk.skala.myapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MyappApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyappApplication.class, args);
    }
}