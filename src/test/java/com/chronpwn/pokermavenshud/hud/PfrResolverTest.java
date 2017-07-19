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
class PfrResolverTest extends StatisticResolverTest {

    @Test
    void resolveAllForCurrentHand_ShouldIncrementPfrForPlayersThatRaisedWhenPuttingExtraChipsInPot() {
        Set<Player> affectedPlayers = resolver.resolveAllForCurrentHand();
        assertEquals(2, affectedPlayers.size());
        for (Player player : affectedPlayers) {
            assertEquals(1, player.getHudStats().getPfr().getHits());
        }
    }

    @Test
    void resolveStatistic_ShouldIncrementPfr() {
        Player player = HandHistoryCreator.looseAggressivePlayer();
        resolver.resolveStatistic(player);
        assertEquals(1, player.getHudStats().getPfr().getHits());
    }

    @Override
    void replaceHandHistory(HandHistory handHistory) {
        if (resolver == null) {
            resolver = new PfrResolver();
        }
        resolver.setHandHistory(handHistory);
    }
}