package com.chronpwn.pokermavenshud.factory;

import com.chronpwn.pokermavenshud.domain.HandHistory;
import com.chronpwn.pokermavenshud.domain.Player;
import com.chronpwn.pokermavenshud.domain.PokerAction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Armin Naderi.
 */
public class HandHistoryCreator implements TestFixtureCreator<HandHistory> {
    private static final int OPEN_RAISE_BB_COUNT = 3;
    private static final int THREE_BET_IN_POSITION_BB_COUNT = 9;
    private static final int DISCRETE_ACTION_AMOUNT = 0;

    @Override
    public HandHistory validInstance() {
        HandHistory hand = lazyInstance();

        hand.setPlayers(createPlayers());
        hand.setPokerActions(createPokerActions());

        return hand;
    }

    @Override
    public HandHistory invalidInstance() {
        return new HandHistory();
    }

    @Override
    public HandHistory lazyInstance() {
        HandHistory hand = new HandHistory();

        hand.setId(1L);
        hand.setRawText("");
        hand.setTableName("Cyan");
        hand.setGameId("NL Hold'em");
        hand.setGameDescription("NL Hold'em");
        hand.setPlayerCount(9);

        return hand;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(HandHistory.class);
    }

    private Set<Player> createPlayers() {
        Set<Player> players = new HashSet<>();

        players.add(nitPlayer());
        players.add(looseAggressivePlayer());
        players.add(tightAggressivePlayer());
        players.add(loosePassivePlayer());

        return players;
    }

    private static Player playerFactory(Long id, String username, double balance) {
        Player player = new Player();
        player.setId(id);
        player.setUsername(username);
        player.setBalance(balance);
        return player;
    }

    private List<PokerAction> createPokerActions() {
        List<PokerAction> preflopActions = new ArrayList<>();

        preflopActions.add(pokerActionFactory(1L, looseAggressivePlayer(), PokerAction.Type.RAISE,
                PokerAction.Street.PRE_FLOP, OPEN_RAISE_BB_COUNT, 0));
        preflopActions.add(pokerActionFactory(2L, nitPlayer(), PokerAction.Type.RAISE,
                PokerAction.Street.PRE_FLOP, THREE_BET_IN_POSITION_BB_COUNT, 1));
        preflopActions.add(pokerActionFactory(3L, tightAggressivePlayer(), PokerAction.Type.FOLD,
                PokerAction.Street.PRE_FLOP, DISCRETE_ACTION_AMOUNT, 2));
        preflopActions.add(pokerActionFactory(4L, loosePassivePlayer(), PokerAction.Type.FOLD,
                PokerAction.Street.PRE_FLOP, DISCRETE_ACTION_AMOUNT, 3));
        preflopActions.add(pokerActionFactory(5L, looseAggressivePlayer(), PokerAction.Type.FOLD,
                PokerAction.Street.PRE_FLOP, DISCRETE_ACTION_AMOUNT, 4));

        return preflopActions;
    }

    private PokerAction pokerActionFactory(Long id, Player player, PokerAction.Type type,
                                           PokerAction.Street street, float amount, int ordinal) {
        PokerAction pokerAction = new PokerAction(player, type, street, amount, ordinal);
        pokerAction.setId(id);
        return pokerAction;
    }

    public static Player looseAggressivePlayer() {
        return playerFactory(2L, "playthenuts", 0);
    }

    public static Player nitPlayer() {
        return playerFactory(1L, "yvan316", 25);
    }

    public static Player tightAggressivePlayer() {
        return playerFactory(3L, "mrgle", 0);
    }

    private Player loosePassivePlayer() {
        return playerFactory(4L, "mike", 500);
    }
}
