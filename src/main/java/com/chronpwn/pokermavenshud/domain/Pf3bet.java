package com.chronpwn.pokermavenshud.domain;

/**
 * Preflop3Bet - how often the player 3bets preflop given the opportunity to do so.
 *
 * However, this is not like traditional 3bet stats, in that the number can be higher than PFR.
 *
 * This occurs when the player does not have that many opportunities to 3bet, and more situations where they can
 * make a single raise (when their opens are always UTG, for example).
 *
 * @author Armin Naderi.
 */
public class Pf3bet implements Statistic {

    private int opportunities;
    private int hits;

    public void incrementOpportunities() {
        this.opportunities += 1;
    }

    public void incrementHits() {
        this.hits += 1;
    }

    @Override
    public float getValue() {
        if (opportunities == 0) {
            return 0;
        }
        return (float) hits / opportunities;
    }

    public int getOpportunities() {
        return opportunities;
    }

    public void setOpportunities(int opportunities) {
        this.opportunities = opportunities;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }
}
