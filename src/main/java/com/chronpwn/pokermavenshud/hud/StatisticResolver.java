package com.chronpwn.pokermavenshud.hud;

import com.chronpwn.pokermavenshud.domain.HandHistory;
import com.chronpwn.pokermavenshud.domain.Player;
import com.chronpwn.pokermavenshud.domain.PokerAction;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Resolves a single statistic within the {@link com.chronpwn.pokermavenshud.domain.HudStats} class.
 * @author Armin Naderi.
 */
public abstract class StatisticResolver {
    HandHistory handHistory;

    /**
     * Resolves the HUD stats of all players in the provided {@link HandHistory} for the given statistic,
     * if applicable.
     *
     * Note that this method not only returns the modified {@link Player}s, but also modifies the relevant statistic
     * by reference.
     *
     * @return the players that had their statistic modified
     */
    public abstract Set<Player> resolveAllForCurrentHand();

    /**
     * Resolves a single statistic retrieved from a single hand history.
     * Note that this method is pass by reference, not value.
     * @param player a player that may or may not have a statistic affected by the handHistory field
     */
    public abstract void resolveStatistic(Player player);

    List<PokerAction> preflopActions() {
        return handHistory.getPokerActions().stream()
                .filter(pokerAction -> pokerAction.getStreet() == PokerAction.Street.PRE_FLOP)
                .collect(Collectors.toList());
    }

    List<PokerAction> preflopActions(Player player) {
        return handHistory.getPokerActions().stream()
                .filter(pokerAction -> pokerAction.getStreet() == PokerAction.Street.PRE_FLOP)
                .filter(pokerAction -> pokerAction.getPlayer().equals(player))
                .collect(Collectors.toList());
    }

    void sortOrdinal(List<PokerAction> pokerActions) {
        pokerActions.sort((o1, o2) -> {
            if (o1.getOrdinal() > o2.getOrdinal()) {
                return 1;
            } else if (o1.getOrdinal() < o2.getOrdinal()) {
                return -1;
            }
            throw new IllegalArgumentException("No two PokerActions can have the same ordinal!");
        });
    }

    int getIndexOfFirstRaise(List<PokerAction> pokerActions) {
        for (PokerAction pokerAction : pokerActions) {
            if (pokerAction.getType() == PokerAction.Type.RAISE) {
                return pokerAction.getOrdinal();
            }
        }
        return -1;
    }

    public void setHandHistory(HandHistory handHistory) {
        this.handHistory = handHistory;
    }
}
