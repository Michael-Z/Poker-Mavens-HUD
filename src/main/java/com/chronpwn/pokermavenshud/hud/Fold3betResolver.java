package com.chronpwn.pokermavenshud.hud;

import com.chronpwn.pokermavenshud.domain.HandHistory;
import com.chronpwn.pokermavenshud.domain.Player;
import com.chronpwn.pokermavenshud.domain.PokerAction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Resolves the preflop fold versus 3bet statistic.
 * @author Armin Naderi.
 */
public class Fold3betResolver extends StatisticResolver {

    private List<PokerAction> preflopActions;

    @Override
    public Set<Player> resolveAllForCurrentHand() {
        Set<Player> modifiedPlayers = new HashSet<>();

        int firstRaiseIndex = getIndexOfFirstRaise(preflopActions);

        // If no player raised, this stat does not affect any players within this hand
        if (firstRaiseIndex == -1) {
            return modifiedPlayers;
        }


        Player openRaiser = preflopActions.get(firstRaiseIndex).getPlayer();


        // Check whether any action after the open raiser was a 3bet
        for (int i = firstRaiseIndex + 1; i < preflopActions.size(); i++) {
            PokerAction pokerAction = preflopActions.get(i);

            // If a player did 3bet the open raiser preflop
            if (pokerAction.getType() == PokerAction.Type.RAISE) {

                // We know they got 3bet, so increment 3bet count
                openRaiser.getHudStats().getFold3bet().incrementThreeBetCount();

                // Find the index of the response action of the preflop raiser
                int responseIndex = findNextInstanceIndex(preflopActions, firstRaiseIndex);

                if (responseIndex == -1) {
                    throw new IllegalArgumentException("The open raiser must respond to a 3bet!");
                }

                // The response can be a fold, call, or raise (re-raise)
                PokerAction threeBetResponse = preflopActions.get(responseIndex);

                // If their response was a fold, increment their fold hits
                if (threeBetResponse.getType() == PokerAction.Type.FOLD) {
                    openRaiser.getHudStats().getFold3bet().incrementFolds();
                    modifiedPlayers.add(openRaiser);
                    // There exist no other players in this hand that can have this stat affected, so return
                    return modifiedPlayers;
                }
            }
        }


        // Returns empty set, means that there was an open raise but nobody 3bet the opener
        return modifiedPlayers;
    }

    @Override
    public void resolveStatistic(Player player) {
        int firstRaiseIndex = getIndexOfFirstRaise(preflopActions);

        if (firstRaiseIndex == -1) {
            // No raise occurred during this hand, so return
            return;
        }

        PokerAction pokerAction = preflopActions.get(firstRaiseIndex);

        if (!pokerAction.getPlayer().equals(player)) {
            // The player in question was not the open raiser, so this stat is not applicable for this hand
            return;
        }

        // If the player had to make another preflop action after opening, it means they were 3bet
        int responseIndex = findNextInstanceIndex(preflopActions, firstRaiseIndex);

        if (responseIndex == -1) {
            // They were not 3bet, so return
            return;
        }

        // We know they got 3bet, so increment 3bet count
        player.getHudStats().getFold3bet().incrementThreeBetCount();

        // The response can be a fold, call, or raise (re-raise)
        PokerAction threeBetResponse = preflopActions.get(responseIndex);

        // If their response was a fold, increment their fold hits
        if (threeBetResponse.getType() == PokerAction.Type.FOLD) {
            player.getHudStats().getFold3bet().incrementFolds();
        }
    }

    private int findNextInstanceIndex(List<PokerAction> pokerActions, int startingOrdinal) {
        PokerAction initialAction = pokerActions.get(startingOrdinal);
        Player initialPlayer = initialAction.getPlayer();

        for (PokerAction pokerAction : pokerActions) {
            if (pokerAction.getOrdinal() > startingOrdinal && pokerAction.getPlayer().equals(initialPlayer)) {
                return pokerAction.getOrdinal();
            }
        }
        return -1;
    }

    @Override
    public void setHandHistory(HandHistory handHistory) {
        super.setHandHistory(handHistory);
        this.preflopActions = preflopActions();
    }
}
