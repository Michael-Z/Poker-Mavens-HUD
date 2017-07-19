package com.chronpwn.pokermavenshud;

import com.chronpwn.pokermavenshud.controller.ConsoleTabController;
import com.chronpwn.pokermavenshud.controller.PlayTabController;
import com.chronpwn.pokermavenshud.view.FxmlView;
import com.chronpwn.pokermavenshud.view.StageManager;
import com.teamdev.jxbrowser.chromium.BrowserCore;
import com.teamdev.jxbrowser.chromium.BrowserPreferences;
import com.teamdev.jxbrowser.chromium.LoggerProvider;
import com.teamdev.jxbrowser.chromium.internal.Environment;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.logging.Level;

@SpringBootApplication
public class Main extends Application {
    private ConfigurableApplicationContext springContext;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() throws Exception {
        springContext = bootstrapSpringApplicationContext();
        // Must be set otherwise the mobile view may be shown occasionally
        BrowserPreferences.setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
        // On Mac OS X Chromium engine must be initialized in non-UI thread.
        if (Environment.isMac()) {
            BrowserCore.initialize();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Initialize the StageManager component lazily
        StageManager stageManager = springContext.getBean(StageManager.class, stage);
        // Display the initial scene
        stageManager.switchScene(FxmlView.MAIN);
    }

    @Override
    public void stop() throws Exception {
        // With JavaFX integration, the browser is not disposed of automatically
        if (BrowserCore.isInitialized()) {
            // Must be executed before the context is closed, otherwise the bean will not be retrievable
            ConsoleTabController consoleTabController = springContext.getBean(ConsoleTabController.class);
            consoleTabController.getBrowser().dispose();
            PlayTabController playTabController = springContext.getBean(PlayTabController.class);
            playTabController.getBrowser().dispose();
            BrowserCore.shutdown();
        }
        springContext.close();
    }

    /**
     * <p>Configures a @{@link SpringApplication} to be compatible with this application's technology stack, and
     * then runs it.</p>
     * @return a running {@link org.springframework.context.ApplicationContext}
     */
    private ConfigurableApplicationContext bootstrapSpringApplicationContext() {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Main.class);
        // Main method arguments passed into Application.launch(args) are retrieved
        String[] args = getParameters().getRaw().stream().toArray(String[]::new);
        /* By default Spring Boot assumes that it will be run in a server environment with no monitor available.
         Headless. Thus, with a double negative, we tell Spring Boot that there is a head or monitor. This is
         required for TestFX Functional Tests to work. */
        builder.headless(false);
        return builder.run(args);
    }
}