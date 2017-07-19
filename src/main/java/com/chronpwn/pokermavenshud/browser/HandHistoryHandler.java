package com.chronpwn.pokermavenshud.browser;

import com.chronpwn.pokermavenshud.controller.PlayTabController;
import com.chronpwn.pokermavenshud.domain.HandHistory;
import com.chronpwn.pokermavenshud.domain.Player;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Parses incoming innerText data to domain objects, saves to the database if necessary.
 * @author Armin Naderi.
 */
@Component
public class HandHistoryHandler {
    private static final String EMPTY_INPUT = "";

    private PlayTabController playTabController;
    private StatementParser statementParser;
    private PlayerCache playerCache;
    private DataAggregator dataAggregator;

    private String previousInput;
    private String currentHandInput;
    private HandHistory currentHand;

    public HandHistoryHandler(PlayTabController playTabController,
                              StatementParser statementParser,
                              PlayerCache playerCache,
                              DataAggregator dataAggregator)
    {
        this.playTabController = playTabController;
        this.statementParser = statementParser;
        this.playerCache = playerCache;
        this.dataAggregator = dataAggregator;
        initializeNonDependencies();
    }

    /* Synchronizing only this method seems to be working, as it is the single access point for all other
     fields and components, and when the components are called, then return values are waited for, which makes
     them not need to be synchronized if this method is already. */
    public synchronized void handle(String innerText) {
        // Activated if the history is just starting to be collected, or a new hand has started
        if (previousInput.equals(EMPTY_INPUT)) {
            previousInput = innerText;
            currentHandInput += innerText;
            System.out.println(innerText);
            // This works since if the input was empty, the innerText will always be equal to the difference
            String[] separatedStatements = innerText.split("\\n");

            if (isContainsNewHandHeader(separatedStatements)) {
                /* Update the current hand object with the new hand headers. The hand history does not need to
                 be persisted as some of its required fields are not set yet. */
                currentHand = statementParser.parseHandHistoryHeaders(separatedStatements);
                // Add all players present at the start of the hand to the user interface
                updateHudInterface(currentHand.getPlayers());
            }
            return;
        }

        doHandle(innerText);
    }

    private void doHandle(String innerText) {
        String difference = StringUtils.difference(previousInput, innerText);
        System.out.println(difference);

        /* The #addBettingActionsToHandHistory takes a long time to execute, and if this is not called as fast as
         possible, concurrency problems arise as this value will not be updated quickly enough and cause the
         difference value to drift. This happened when this line of code was placed at the end of the
         #addBettingActionsToHandHistory method. This occurs since the JavaScript code does not care whether
         this method has finished execution or not, it just fires whenever it is able to */
        previousInput = innerText;
        // Append before end of hand check, since the end of hand statements are required during data aggregation
        currentHandInput += difference;

        // Separate each statement through its new-line separator
        String[] separatedStatements = difference.split("\\n");

        /* Attempt to aggregate the data if an end hand header is present within the difference. This allows the
         last full hand to be recorded when players exit the table. */
        if (isContainsEndHandHeader(separatedStatements)) {
            // Ignore the text if it is not complete as it will not be useful for hud stats
            /* Wow!! READ THIS CAREFULLY: Exceptions thrown within a Java-JavaScript adapter class may not be
             recorded explicitly! In my case, a NPE was being thrown due to a Collection not being initialized,
             and the JavaScript bridge simply exited with no error message. Be careful. */
            if (isHandDescriptionComplete()) {
                // Set the remaining fields to complete the current hand object
                currentHand.setRawText(currentHandInput);
                currentHand.setPlayerCount(currentHand.getPlayers().size());
                /* Aggregate all data based on the current hand. This will persist the hand and perform
                 post-processing actions that affect related objects. */
                dataAggregator.persistAndPropagate(currentHand);
            }
            /* Cache the players at the end of the hand so that players that stay until the next hand do not
             have to have their HUD stats recomputed when the hand history headers are parsed. */
            playerCache.replaceCacheEntries(currentHand.getPlayers());
            // Reset the data as a new hand is about to start, thus fresh statistics are required
            resetData();
            return;
        }

        // Otherwise, convert the statements into street by street data
        for (String statement : separatedStatements) {
            // Continue to populate the current hand object's fields...
            statementParser.attemptPokerActionAddition(currentHand, statement);
        }
    }

    private boolean isHandDescriptionComplete() {
        return isInputContainsCompleteHandDescription(currentHandInput);
    }

    private boolean isInputContainsCompleteHandDescription(String input) {
        String[] statements = input.split("\\n");
        boolean containsEndHandHeader = isContainsEndHandHeader(input);

        /* A complete hand will not only have the header, but also a footer. If the footer is not checked
         for any new hand will return true, which is not the desired behavior. */
        return isContainsNewHandHeader(statements) && containsEndHandHeader;
    }

    private void initializeNonDependencies() {
        resetData();
        /* Must be set at initialization, otherwise the current hand field will be called when it is null and
         the player cache is being set. */
        currentHand = new HandHistory();
    }

    private void resetData() {
        statementParser.resetData();
        previousInput = EMPTY_INPUT;
        currentHandInput = EMPTY_INPUT;
    }

    private boolean isContainsEndHandHeader(String[] statements) {
        for (String statement : statements) {
            if (isContainsEndHandHeader(statement)) {
                return true;
            }
        }
        return false;
    }

    private boolean isContainsEndHandHeader(String statement) {
        // The footer contains the String 'Rake'
        return statement.contains("Rake");
    }

    private boolean isContainsNewHandHeader(String[] statements) {
        for (String statement : statements) {
            // If a new hand has started
            if (statement.trim().equals("Site: TokenBets")) {
                return true;
            }
        }
        return false;
    }

    private void updateHudInterface(Set<Player> newPlayers) {

        ObservableList<Player> playersOnUi = playTabController.getPlayersList().getItems();

        for (Player newPlayer : newPlayers) {
            /* TODO: Temporary solution as it is taking too long to figure out why the parent field is getting nullified
         from the point at which the VPIP object is being created */
            newPlayer.getHudStats().getVpip().setParent(newPlayer.getHudStats());
            newPlayer.getHudStats().getPfr().setParent(newPlayer.getHudStats());

            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    if (playersOnUi.contains(newPlayer)) {
                        // Update the stats of an existing player at the table
                        playersOnUi.replaceAll(currentPlayer -> {
                            if (currentPlayer.getId().equals(newPlayer.getId())) {
                                currentPlayer = newPlayer;
                            }
                            return currentPlayer;
                        });
                    } else {
                        // Add a player that has joined the table
                        playersOnUi.add(newPlayer);
                    }

                    /* java.util.ConcurrentModificationException may be thrown if using a for-each loop, as
                     the collection has elements removed while being looped through */
                    playersOnUi.removeIf(oldPlayer -> !newPlayers.contains(oldPlayer));
                }
            });
        }
    }
}