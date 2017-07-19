package com.chronpwn.pokermavenshud.hud;

import com.chronpwn.pokermavenshud.domain.HandHistory;
import com.chronpwn.pokermavenshud.domain.Player;
import com.chronpwn.pokermavenshud.domain.PokerAction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Resolves how often the player 3bets preflop given the opportunity to do so.
 *
 * Has to not only increment the sample size (opportunities), but also the number of hits.
 *
 * @author Armin Naderi.
 */
public class Pf3betResolver extends StatisticResolver {

    private List<PokerAction> preflopActions;

    @Override
    public Set<Player> resolveAllForCurrentHand() {
        Set<Player> modifiedPlayers = new HashSet<>();

        int firstRaiseIndex = getIndexOfFirstRaise(preflopActions);

        // No raise occurred within this hand, thus there were no 3bet opportunities. Return empty set
        if (firstRaiseIndex == -1) {
            return modifiedPlayers;
        }


        // Start at the very next action from the open raiser
        for (int i = firstRaiseIndex + 1; i < preflopActions.size(); i++) {
            PokerAction pokerAction = preflopActions.get(i);

            Player player = pokerAction.getPlayer();

            modifiedPlayers.add(player);

            // This player had the opportunity to 3bet the open raiser
            player.getHudStats().getPf3bet().incrementOpportunities();

                /* This player actually 3bet the open raiser.  */
            if (pokerAction.getType() == PokerAction.Type.RAISE) {
                player.getHudStats().getPf3bet().incrementHits();
                // This means that all remaining players do not have the 3bet opportunity, exit loop
                break;
            }
        }

        return modifiedPlayers;
    }

    @Override
    public void resolveStatistic(Player player) {
        int firstRaiseIndex = getIndexOfFirstRaise(preflopActions);

        // No raise occurred within this hand, thus there were no 3bet opportunities. Return empty set
        if (firstRaiseIndex == -1) {
            return;
        }

        // Start at the very next action from the open raiser
        for (int i = firstRaiseIndex + 1; i < preflopActions.size(); i++) {
            PokerAction pokerAction = preflopActions.get(i);

            if (pokerAction.getType() == PokerAction.Type.RAISE) {
                // If not, they did not have an opportunity to 3bet, someone else took the opportunity
                if (pokerAction.getPlayer().equals(player)) {
                    player.getHudStats().getPf3bet().incrementOpportunities();
                    player.getHudStats().getPf3bet().incrementHits();
                    // Return as no other preflop 3bet opportunities exist within this hand
                    return;
                }
            }

            // If they had the opportunity to 3bet, but did not do so
            if (pokerAction.getPlayer().equals(player)) {
                player.getHudStats().getPf3bet().incrementOpportunities();
                return;
            }
        }

    }

    @Override
    public void setHandHistory(HandHistory handHistory) {
        super.setHandHistory(handHistory);
        this.preflopActions = preflopActions();
        this.sortOrdinal(preflopActions);
    }
}
