package com.himiko;

import com.himiko.game.utils.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MainApplication {
    public static User serverUserProfile;
    public static ConfigurableApplicationContext appConfig;

    public static void main(String[] args) {
        serverUserProfile = new User("Server", 0, 0, 0);
        //App Config
        appConfig = new SpringApplication(MainApplication.class).run(args);
    }
}
