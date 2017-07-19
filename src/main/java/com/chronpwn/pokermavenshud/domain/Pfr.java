package com.chronpwn.pokermavenshud.domain;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * PreFlopRaise
 * @author Armin Naderi.
 */
@Embeddable
public class Pfr implements Statistic {
    private static final int USEFUL_THRESHOLD = 100;

    @Transient
    private HudStats parent;
    private int hits;

    public void increment() {
        hits += 1;
    }

    @Override
    public float getValue() {
        if (parent.getSampleSize() == 0) {
            return 0;
        }
        return (float) hits / parent.getSampleSize();
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public void setParent(HudStats parent) {
        this.parent = parent;
    }
}
