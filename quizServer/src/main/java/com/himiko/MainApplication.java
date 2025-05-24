package com.himiko;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MainApplication {
    public static ConfigurableApplicationContext appConfig;

    public static void main(String[] args) {
        appConfig = new SpringApplication(MainApplication.class).run(args);
    }
}
