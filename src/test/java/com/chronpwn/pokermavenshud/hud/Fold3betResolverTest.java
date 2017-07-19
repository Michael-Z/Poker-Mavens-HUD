package com.chronpwn.pokermavenshud.hud;

import com.chronpwn.pokermavenshud.domain.HandHistory;
import com.chronpwn.pokermavenshud.domain.Player;
import com.chronpwn.pokermavenshud.factory.HandHistoryCreator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Armin Naderi.
 */
class Fold3betResolverTest extends StatisticResolverTest {

    @Test
    void resolveAllForCurrentHand_ShouldIncrementFoldVs3BetStatistic() {
        Set<Player> affectedPlayers = resolver.resolveAllForCurrentHand();
        assertEquals(1, affectedPlayers.size());
        for (Player player : affectedPlayers) {
            assertEquals(1f, player.getHudStats().getFold3bet().getValue());
        }
    }

    @Test
    void resolveStatistic_ShouldIncrementFoldVs3betStatistic() {
        Player player = HandHistoryCreator.looseAggressivePlayer();
        assertEquals(0f, player.getHudStats().getFold3bet().getValue());
        resolver.resolveStatistic(player);
        assertEquals(1f, player.getHudStats().getFold3bet().getValue());
    }

    @Override
    void replaceHandHistory(HandHistory handHistory) {
        if (resolver == null) {
            resolver = new Fold3betResolver();
        }
        resolver.setHandHistory(handHistory);
    }
}