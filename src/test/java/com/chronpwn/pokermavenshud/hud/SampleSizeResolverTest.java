package com.chronpwn.pokermavenshud.hud;

import com.chronpwn.pokermavenshud.domain.HandHistory;
import com.chronpwn.pokermavenshud.domain.Player;
import com.chronpwn.pokermavenshud.factory.HandHistoryCreator;
import com.chronpwn.pokermavenshud.factory.TestFixtureFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests possible edge cases within the {@link SampleSizeResolver}
 * @author Armin Naderi.
 */
class SampleSizeResolverTest extends StatisticResolverTest {

    @Test
    @DisplayName("Condition: When provided a hand history where no poker actions occurred.")
    void resolveAllForCurrentHand_ShouldPerformNoOp() {
        replaceHandHistory(TestFixtureFactory.createLazyInstance(HandHistory.class));
        Set<Player> affectedPlayers = resolver.resolveAllForCurrentHand();
        assertEquals(0, affectedPlayers.size());
    }

    @Test
    void resolveAllForCurrentHand_ShouldIncrementSampleSizeForAll() {
        Set<Player> affectedPlayers = resolver.resolveAllForCurrentHand();
        for (Player player : affectedPlayers) {
            assertEquals(1, player.getHudStats().getSampleSize());
        }
    }

    @Test
    void resolveAllForCurrentHand_ShouldReturnTheCorrectSet() {
        Set<Player> affectedPlayers = resolver.resolveAllForCurrentHand();
        assertEquals(4, affectedPlayers.size());
    }

    @Test
    void resolveStatistic_ShouldIncrementSampleSize() {
        Player player = HandHistoryCreator.looseAggressivePlayer();
        resolver.resolveStatistic(player);
        assertEquals(1, player.getHudStats().getSampleSize());
    }

    @Override
    void replaceHandHistory(HandHistory handHistory) {
        if (resolver == null) {
            resolver = new SampleSizeResolver();
        }
        resolver.setHandHistory(handHistory);
    }
}