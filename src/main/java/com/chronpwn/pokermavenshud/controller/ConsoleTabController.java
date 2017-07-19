package com.chronpwn.pokermavenshud.controller;

import com.chronpwn.pokermavenshud.browser.HandHistoryHandler;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.dom.By;
import com.teamdev.jxbrowser.chromium.dom.DOMDocument;
import com.teamdev.jxbrowser.chromium.dom.DOMElement;
import com.teamdev.jxbrowser.chromium.dom.DOMNode;
import com.teamdev.jxbrowser.chromium.events.*;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

//iframe#poker-frame iframe#client-frame
//.infopanel ~ .infotabs
//.historyinfo .memo.noselect

@Component
public class ConsoleTabController {
    private static final Logger LOG = LoggerFactory.getLogger(ConsoleTabController.class);

    @Autowired
    private HandHistoryHandler handHistoryHandler;

    @FXML
    public Button collectDataButton;
    @FXML
    private ListView<String> missionsList;
    @FXML
    private BrowserView browserView;
    /* Note: If user agent is not provided on the JavaFX native WebEngine, the captcha will not be displayed,
     which on login gives the error: "HTTP ERROR: 405 Problem accessing /post. Reason: HTTP method POST
     is not supported by this URL - JETTY". HOWEVER: Browser by TeamDev seems to be setting the user-agent
     String automatically, thus it is not required anymore. */
    private Browser browser;

    public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                ScrollBar scrollBar = getVerticalScrollbar(missionsList);
                scrollBar.valueProperty().addListener(ConsoleTabController.this::scrolled);
            }
        });
        missionsList.setItems(getItemNames(100));

        browser = browserView.getBrowser();

        browser.addConsoleListener(event -> {
            if (event.getLevel().equals(ConsoleEvent.Level.ERROR)) {
                System.out.println("Error: " + event.getMessage());
            }
        });

        browser.addScriptContextListener(new ScriptContextAdapter() {
            @Override
            public void onScriptContextCreated(ScriptContextEvent event) {
                Browser browser = event.getBrowser();
                JSValue window = browser.executeJavaScriptAndReturnValue("window");
                window.asObject().setProperty("hud", handHistoryHandler);
            }
        });


        browser.loadURL("https://poker.tokenbets.com:8087");
    }

    @FXML
    private void onCollectDataClicked(MouseEvent event) {
        DOMDocument document = browser.getDocument();
        DOMNode root = document.findElement(By.tagName("script"));

        Resource resource = new ClassPathResource("js/history-listener.js");
        String contents = null;
        try {
            contents = readFile(resource.getInputStream(), Charset.forName("utf-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        DOMNode textNode = document.createTextNode(contents);
        DOMElement paragraph = document.createElement("script");
        paragraph.appendChild(textNode);
        root.getParent().appendChild(paragraph);
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
        String selectedItem = missionsList.getSelectionModel().getSelectedItem();

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

    private ScrollBar getVerticalScrollbar(ListView<?> listView) {
        ScrollBar result = null;
        for (Node n : listView.lookupAll(".scroll-bar")) {
            if (n instanceof ScrollBar) {
                ScrollBar bar = (ScrollBar) n;
                if (bar.getOrientation().equals(Orientation.VERTICAL)) {
                    result = bar;
                }
            }
        }
        return result;
    }

    private void scrolled(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        int newLimit = missionsList.getItems().size() + 100;
        double value = newValue.doubleValue();
        ScrollBar bar = getVerticalScrollbar(missionsList);
        if (value == bar.getMax()) {
            missionsList.setItems(getItemNames(newLimit));
//            double targetValue = value * items.size();
//            addPersons();
//            bar.setValue(targetValue / items.size());
        }
    }

    private ObservableList<String> getItemNames(int limit) {
//        List<String> itemNames = itemService.findAll(new PageRequest(0, limit)).stream()
//                .map(Item::getName)
//                .collect(Collectors.toList());
//        return FXCollections.observableArrayList(itemNames);
        return FXCollections.observableArrayList();
    }

    public ListView<String> getMissionsList() {
        return missionsList;
    }

    public Browser getBrowser() {
        return browser;
    }

    static String readFile(InputStream inputStream, Charset encoding)
            throws IOException
    {

        StringWriter stringWriter = new StringWriter();
        IOUtils.copy(inputStream, stringWriter, encoding);
        return stringWriter.toString();
    }
}
