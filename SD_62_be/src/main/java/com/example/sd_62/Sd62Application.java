package com.example.sd_62;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // ✅ BẮT BUỘC CHO CLEANUP TASK
public class Sd62Application {
    public static void main(String[] args) {
        SpringApplication.run(Sd62Application.class, args);
    }
}