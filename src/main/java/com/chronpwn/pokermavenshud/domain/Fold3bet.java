package com.chronpwn.pokermavenshud.domain;

/**
 * Fold versus preflop 3bet when open raising.
 * Counts isolation raises as open raises too.
 * @author Armin Naderi.
 */
public class Fold3bet implements Statistic {
    private int amountPlayerHasBeenThreeBet;
    private int foldHits;

    public void incrementThreeBetCount() {
        this.amountPlayerHasBeenThreeBet += 1;
    }

    public void incrementFolds() {
        this.foldHits += 1;
    }

    @Override
    public float getValue() {
        if (amountPlayerHasBeenThreeBet == 0) {
            return 0;
        }
        return (float) foldHits / amountPlayerHasBeenThreeBet;
    }

    public int getAmountPlayerHasBeenThreeBet() {
        return amountPlayerHasBeenThreeBet;
    }

    public void setAmountPlayerHasBeenThreeBet(int amountPlayerHasBeenThreeBet) {
        this.amountPlayerHasBeenThreeBet = amountPlayerHasBeenThreeBet;
    }

    public int getFoldHits() {
        return foldHits;
    }

    public void setFoldHits(int foldHits) {
        this.foldHits = foldHits;
    }
}
