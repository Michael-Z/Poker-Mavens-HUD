package com.chronpwn.pokermavenshud.controller;

import com.chronpwn.pokermavenshud.component.PlayerCellFactory;
import com.chronpwn.pokermavenshud.domain.Player;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//iframe#poker-frame iframe#client-frame
//.infopanel ~ .infotabs
//.historyinfo .memo.noselect

@Component
public class PlayTabController {
    private static final Logger LOG = LoggerFactory.getLogger(PlayTabController.class);

    private final ObservableList<Player> listViewData = FXCollections.observableArrayList();

    @FXML
    private ListView<Player> playersList;
    @FXML
    private BrowserView browserView;

    /* Note: If user agent is not provided on the JavaFX native WebEngine, the captcha will not be displayed,
     which on login gives the error: "HTTP ERROR: 405 Problem accessing /post. Reason: HTTP method POST
     is not supported by this URL - JETTY". HOWEVER: Browser by TeamDev seems to be setting the user-agent
     String automatically, thus it is not required anymore. */
    private Browser browser;

    public void initialize() {
        playersList.setItems(listViewData);
        playersList.setCellFactory(new PlayerCellFactory());

        browser = browserView.getBrowser();
        browser.loadURL("https://www.tokenbets.com");
    }

    @FXML
    private void onMouseClicked(MouseEvent event) {
        selectListItem();
    }

    @FXML
    private void onEnterReleased(KeyEvent event) {
        if (!event.getCode().equals(KeyCode.ENTER)) {
            return;
        }
        selectListItem();
    }

    private void selectListItem() {
        //        missionOverviewText.clear();
        Player selectedItem = playersList.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            String msg = "User clicked on bad area of list";
            LOG.warn(msg, new NullPointerException(msg));
            return;
        }
    }

    private String getInfo(String selectedItem) {
        String missionInfo = null;

        try {
            missionInfo = selectedItem;
            LOG.info("Sucessfully retrieved mission info for " + selectedItem + "\n");
        } catch (Exception exception) {
            LOG.error("Could not retrieve mission info!", exception);
        }

        return missionInfo;
    }

    public ListView<Player> getPlayersList() {
        return playersList;
    }

    public Browser getBrowser() {
        return browser;
    }
}

