package com.chronpwn.pokermavenshud.hud;

import com.chronpwn.pokermavenshud.domain.HudStats;
import com.chronpwn.pokermavenshud.domain.Player;
import com.chronpwn.pokermavenshud.domain.PokerAction;

import java.util.HashSet;
import java.util.Set;

/**
 * Resolves the sample size statistic.
 * @author Armin Naderi.
 */
public class SampleSizeResolver extends StatisticResolver {

    @Override
    public Set<Player> resolveAllForCurrentHand() {
        Set<Player> updatedPlayers = new HashSet<>();

        /* HandHistory#getPlayers can not be modified directly as it will count players who received a walk
         as well. Thus, all preflop actions must be iterated instead. */
        for (PokerAction pokerAction : preflopActions()) {
            Player player = pokerAction.getPlayer();
            updatedPlayers.add(player);
        }

        /* BE CAREFUL! Trying to optimize this algorithm from the way it originally was caused a massive bug!
         Players that made more than one preflop action had their sample size updated twice! */
        for (Player player : updatedPlayers) {
            incrementSampleSize(player);
        }

        return updatedPlayers;
    }

    @Override
    public void resolveStatistic(Player player) {
        // Only increment if the player made a preflop action
        if (preflopActions(player).size() > 0) {
            incrementSampleSize(player);
        }
    }

    private void incrementSampleSize(Player player) {
        HudStats hudStats = player.getHudStats();
        hudStats.setSampleSize(hudStats.getSampleSize() + 1);
    }
}
