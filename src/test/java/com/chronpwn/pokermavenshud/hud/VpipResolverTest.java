package com.chronpwn.pokermavenshud.hud;

import com.chronpwn.pokermavenshud.domain.HandHistory;
import com.chronpwn.pokermavenshud.domain.Player;
import com.chronpwn.pokermavenshud.factory.HandHistoryCreator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests possible edge cases within the {@link VpipResolver}
 * @author Armin Naderi.
 */
class VpipResolverTest extends StatisticResolverTest {

    @Test
    void resolveAllForCurrentHand_ShouldIncrementVpipForPlayersThatPutExtraChipsInPot() {
        Set<Player> affectedPlayers = resolver.resolveAllForCurrentHand();
        assertEquals(2, affectedPlayers.size());
        for (Player player : affectedPlayers) {
            assertEquals(1, player.getHudStats().getVpip().getHits());
        }
    }

    @Test
    void resolveStatistic_ShouldIncrementVpip() {
        Player player = HandHistoryCreator.looseAggressivePlayer();
        resolver.resolveStatistic(player);
        assertEquals(1, player.getHudStats().getVpip().getHits());

    }

    @Override
    void replaceHandHistory(HandHistory handHistory) {
        if (resolver == null) {
            resolver = new VpipResolver();
        }
        resolver.setHandHistory(handHistory);
    }
}