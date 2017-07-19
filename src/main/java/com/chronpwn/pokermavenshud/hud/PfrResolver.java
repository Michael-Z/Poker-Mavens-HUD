package com.chronpwn.pokermavenshud.hud;

import com.chronpwn.pokermavenshud.domain.Player;
import com.chronpwn.pokermavenshud.domain.PokerAction;

import java.util.HashSet;
import java.util.Set;

/**
 * Resolves the PFR statistic.
 * @author Armin Naderi.
 */
public class PfrResolver extends StatisticResolver {

    @Override
    public Set<Player> resolveAllForCurrentHand() {
        Set<Player> playersToIncreasePfrOf = new HashSet<>();

        for (PokerAction pokerAction : preflopActions()) {
            if (pokerAction.getType() == PokerAction.Type.RAISE) {
                playersToIncreasePfrOf.add(pokerAction.getPlayer());
            }
        }

        for (Player player : playersToIncreasePfrOf) {
            player.getHudStats().getPfr().increment();
        }

        return playersToIncreasePfrOf;
    }

    @Override
    public void resolveStatistic(Player player) {
        for (PokerAction pokerAction : preflopActions(player)) {

            // Increment PFR for this player if they raised preflop at any time during this hand
            if (pokerAction.getType() == PokerAction.Type.RAISE) {
                player.getHudStats().getPfr().increment();
                // Return immediately to avoid increasing this statistic more than once per hand
                return;
            }
        }
    }
}
