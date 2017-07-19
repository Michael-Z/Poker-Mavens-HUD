package com.chronpwn.pokermavenshud.hud;

import com.chronpwn.pokermavenshud.domain.Player;
import com.chronpwn.pokermavenshud.domain.PokerAction;

import java.util.HashSet;
import java.util.Set;

/**
 * Resolves the VPIP statistic.
 * @author Armin Naderi.
 */
public class VpipResolver extends StatisticResolver {

    @Override
    public Set<Player> resolveAllForCurrentHand() {
        Set<Player> playersToIncreaseVpipOf = new HashSet<>();

        PokerAction.Type type;
        // VPIP is only affected by pre-flop actions
        for (PokerAction pokerAction : preflopActions()) {
            type = pokerAction.getType();

            // Increment VPIP for players who either called or raised pre-flop during this hand
            if (type == PokerAction.Type.CALL || type == PokerAction.Type.RAISE) {
                playersToIncreaseVpipOf.add(pokerAction.getPlayer());
            }
        }

        for (Player player : playersToIncreaseVpipOf) {
            player.getHudStats().getVpip().increment();
        }

        return playersToIncreaseVpipOf;
    }

    @Override
    public void resolveStatistic(Player player) {
        PokerAction.Type type;
        for (PokerAction pokerAction : preflopActions(player)) {

            type = pokerAction.getType();

            // If the player either called or raised preflop at any time during this hand
            if (type == PokerAction.Type.CALL || type == PokerAction.Type.RAISE) {
                player.getHudStats().getVpip().increment();
                // Return immediately to avoid increasing this statistic more than once per hand.
                return;
            }
        }
    }
}
