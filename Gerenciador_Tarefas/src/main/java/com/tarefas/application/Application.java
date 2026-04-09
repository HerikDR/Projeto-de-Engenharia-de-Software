package com.tarefas.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.tarefas")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}