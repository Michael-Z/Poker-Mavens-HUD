package com.chronpwn.pokermavenshud.spring;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SpringFxmlLoader {
    private ApplicationContext context;

    @Autowired
    public SpringFxmlLoader(ApplicationContext applicationContext) {
        this.context = applicationContext;
    }

    public Parent load(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        //Move responsibility of creating controllers from JavaFx (default) to Spring
        loader.setControllerFactory(context::getBean);
        loader.setLocation(getClass().getResource(fxmlPath));
        return loader.load();
    }
}
