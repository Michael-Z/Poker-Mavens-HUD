package com.chronpwn.pokermavenshud.view;

import com.chronpwn.pokermavenshud.spring.SpringFxmlLoader;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * A bean (externally registered) that allows an immutable Stage to switch scenes seamlessly.
 */
public class StageManager {
    private static final Logger LOG = getLogger(StageManager.class);
    private final Stage primaryStage;
    private final SpringFxmlLoader springFxmlLoader;

    public StageManager(SpringFxmlLoader springFxmlLoader, Stage stage) {
        this.springFxmlLoader = springFxmlLoader;
        this.primaryStage = stage;
    }

    public void switchScene(final FxmlView fxmlView) {
        Parent parent = loadViewNodeHierarchy(fxmlView.getFxmlFile());
        show(parent, fxmlView.getTitle());
    }

    private void show(final Parent rootNode, String title) {
        Scene scene = prepareScene(rootNode);

        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();

        primaryStage.show();
    }

    private Scene prepareScene(Parent rootNode) {
        Scene scene = primaryStage.getScene();

        if (scene == null) {
            scene = new Scene(rootNode);
        }
        scene.setRoot(rootNode);
        return scene;
    }

    /**
     * Using this method within this class causes a <strong>~1.2 second</strong> delay within the start() method.
     * This is a trade-off of encapsulating all scene switching logic here, but is still fine as almost all of
     * the work is still being done in init().
     */
    private Parent loadViewNodeHierarchy(String fxmlFilePath) {
        Parent rootNode = null;
        try {
            rootNode = springFxmlLoader.load(fxmlFilePath);
            Objects.requireNonNull(rootNode, "A root FXML node must not be null!");
        } catch (IOException e) {
            LOG.error("Unable to load FXML view for path " + fxmlFilePath, e, e.getCause());
            Platform.exit();
        }
        return rootNode;
    }
}
