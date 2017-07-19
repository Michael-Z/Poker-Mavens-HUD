package com.chronpwn.pokermavenshud.repository.listener;

import com.chronpwn.pokermavenshud.domain.HudStats;
import com.chronpwn.pokermavenshud.domain.Player;

import javax.persistence.PostPersist;

/**
 * HudStats is an embeddable so this listener must be attached to the {@link Player} entity instead
 * @author Armin Naderi.
 */
public class HudStatsParentSetter {
    @PostPersist
    public void process(Player parentEntity) {
        HudStats hudStats = parentEntity.getHudStats();
        hudStats.getVpip().setParent(hudStats);
        hudStats.getPfr().setParent(hudStats);
    }
}
