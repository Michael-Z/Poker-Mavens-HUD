package com.chronpwn.pokermavenshud.view;

/**
 * A type-safe enum that encapsulates all views (scenes) that are available to be displayed within stages.
 */
public enum FxmlView {
    MAIN {
        @Override
        String getTitle() {
            return "Poker Mavens HUD";
        }

        @Override
        String getFxmlFile() {
            return "/fxml/Main.fxml";
        }
    };

    abstract String getTitle();
    abstract String getFxmlFile();
}
