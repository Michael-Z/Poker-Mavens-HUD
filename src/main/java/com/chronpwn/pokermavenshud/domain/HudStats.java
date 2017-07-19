package com.chronpwn.pokermavenshud.domain;

import org.apache.commons.lang3.Range;

/**
 * The poker statistics derived from hand histories with a specific player.
 * @author Armin Naderi.
 */
public class HudStats {
    private Properties properties;
    private int sampleSize;

    private Vpip vpip;
    private Pfr pfr;
    private Pf3bet pf3bet;
    private Fold3bet fold3bet;

    HudStats() {
        vpip = new Vpip(this);
        pfr = new Pfr();
        pf3bet = new Pf3bet();
        fold3bet = new Fold3bet();
    }

    public static class Properties {
        private PlayerRange playerRange;
        
        public boolean matches(Properties other) {
            return other.playerRange.equals(this.playerRange);
        }

        public enum PlayerRange {
            HEADS_UP(Range.between(2, 2)),
            THREE_HANDED(Range.between(3, 3)),
            SHORT_HANDED(Range.between(4, 6)),
            FULL_RING(Range.between(7, 9)),
            ANY(Range.between(2, 9));

            private Range<Integer> applicableRange;

            PlayerRange(Range<Integer> applicableRange) {
                this.applicableRange = applicableRange;
            }

            public Range<Integer> getApplicableRange() {
                return applicableRange;
            }
        }

        public PlayerRange getPlayerRange() {
            return playerRange;
        }

        public void setPlayerRange(PlayerRange playerRange) {
            this.playerRange = playerRange;
        }
    }

    public Vpip getVpip() {
        return vpip;
    }

    public void setVpip(Vpip vpip) {
        this.vpip = vpip;
    }

    public Pfr getPfr() {
        return pfr;
    }

    public void setPfr(Pfr pfr) {
        this.pfr = pfr;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public int getSampleSize() {
        return sampleSize;
    }

    public void setSampleSize(int sampleSize) {
        this.sampleSize = sampleSize;
    }

    public Pf3bet getPf3bet() {
        return pf3bet;
    }

    public void setPf3bet(Pf3bet pf3bet) {
        this.pf3bet = pf3bet;
    }

    public Fold3bet getFold3bet() {
        return fold3bet;
    }

    public void setFold3bet(Fold3bet fold3bet) {
        this.fold3bet = fold3bet;
    }
}