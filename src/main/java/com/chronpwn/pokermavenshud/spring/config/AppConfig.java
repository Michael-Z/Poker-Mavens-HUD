package com.chronpwn.pokermavenshud.spring.config;

import com.chronpwn.pokermavenshud.spring.SpringFxmlLoader;
import com.chronpwn.pokermavenshud.view.StageManager;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class AppConfig {

    @Autowired
    private SpringFxmlLoader springFxmlLoader;

    @Bean
    @Lazy //Stage only created after Spring context is bootstrapped
    public StageManager stageManager(Stage stage) {
        return new StageManager(springFxmlLoader, stage);
    }
}
