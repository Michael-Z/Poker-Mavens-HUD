package com.chronpwn.pokermavenshud.hud;

import com.chronpwn.pokermavenshud.domain.HandHistory;
import com.chronpwn.pokermavenshud.domain.Player;
import com.chronpwn.pokermavenshud.domain.PokerAction;
import com.chronpwn.pokermavenshud.factory.HandHistoryCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests edge cases within the {@link Pf3betResolver}.
 * TODO: There seems to be an exception being thrown on the Pf3betResolver at some points, happened after an allin overshove on the flop by a preflop passive player
 * @author Armin Naderi.
 */
class Pf3betResolverTest extends StatisticResolverTest {

    @Test
    void resolveAllForCurrentHand_ShouldIncrementPf3betForPlayersThatReraisedTheFirstRaiserInThePot() {
        Set<Player> affectedPlayers = resolver.resolveAllForCurrentHand();
        assertEquals(1, affectedPlayers.size());
        for (Player player : affectedPlayers) {
            assertEquals(1f, player.getHudStats().getPf3bet().getValue());
        }
    }

    @Test
    @DisplayName("Condition: Button player folded")
    void resolveStatistic_ShouldIncrementOpportunities() {
        modifyHandHistoryToMakeNitFold();
        Player player = HandHistoryCreator.tightAggressivePlayer();
        resolver.resolveStatistic(player);
        assertEquals(1, player.getHudStats().getPf3bet().getOpportunities());
    }

    @Test
    void resolveStatistic_ShouldIncrementPf3bet() {
        Player player = HandHistoryCreator.nitPlayer();
        resolver.resolveStatistic(player);
        // Calculated as (1 hit / 1 opportunity)
        assertEquals(1f, player.getHudStats().getPf3bet().getValue());
    }

    @Override
    void replaceHandHistory(HandHistory handHistory) {
        if (resolver == null) {
            resolver = new Pf3betResolver();
        }
        resolver.setHandHistory(handHistory);
    }

    private void modifyHandHistoryToMakeNitFold() {
        int SECOND_ORDINAL = 1;

        HandHistory handHistory = resolver.handHistory;
        List<PokerAction> pokerActions = handHistory.getPokerActions();
        resolver.sortOrdinal(pokerActions);

        PokerAction replacement = new PokerAction(HandHistoryCreator.nitPlayer(), PokerAction.Type.FOLD, PokerAction.Street.PRE_FLOP, 0f, SECOND_ORDINAL);

        pokerActions.replaceAll(pokerAction -> {
            if (pokerAction.getOrdinal() == SECOND_ORDINAL) {
                return replacement;
            }
            return pokerAction;
        });
    }
}