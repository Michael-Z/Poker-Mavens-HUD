package com.chronpwn.pokermavenshud.browser;

import com.chronpwn.pokermavenshud.domain.HandHistory;
import com.chronpwn.pokermavenshud.domain.HudStats;
import com.chronpwn.pokermavenshud.domain.Player;
import com.chronpwn.pokermavenshud.domain.PokerAction;
import com.chronpwn.pokermavenshud.hud.StatisticResolver;
import com.chronpwn.pokermavenshud.repository.HandHistoryRepository;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Handles the persistence and transformation of hand-history-related objects.
 * @author Armin Naderi.
 */
@Component
public class DataAggregator {

    private HandHistoryRepository handHistoryRepository;

    private List<StatisticResolver> statisticResolvers;
    private HudStats.Properties hudStatsFilter;

    public DataAggregator(HandHistoryRepository handHistoryRepository) {
        this.handHistoryRepository = handHistoryRepository;
        statisticResolvers = loadHudUpdaters();
        hudStatsFilter = defaultHudStatsConfiguration();
    }

    HudStats computeHudStats(Player player) {
        for (HandHistory handHistory : handHistoryRepository.findAll()) {

            if (isConfigurationAcceptsHandHistory(handHistory)) {

                for(StatisticResolver statisticResolver : statisticResolvers) {
                    statisticResolver.setHandHistory(handHistory);
                    statisticResolver.resolveStatistic(player);
                }
            }
        }

        return player.getHudStats();
    }

    /**
     * @param handHistory a hand history with all fields set (and valid)
     */
    void persistAndPropagate(HandHistory handHistory) {
        handHistoryRepository.save(handHistory);

        /* Update the transient HUD stats, but do not save the players to the database, as the modified fields
         will be wiped on the next retrieval. This makes the algorithm more efficient by not having to recompute
         the HUD stats by iterating over all hand histories whenever a new hand starts. It acts as a cache for
         players who have had their hand histories recorded consecutively. */
        this.updateHudStatsForAffectedPlayers(handHistory);
    }

    private void updateHudStatsForAffectedPlayers(HandHistory handHistory) {
        Set<Player> affectedPlayers = new HashSet<>();

        for(StatisticResolver statisticResolver : statisticResolvers) {
            // Hand history must be set JIT (just in time) because it is constantly changing
            statisticResolver.setHandHistory(handHistory);
            affectedPlayers.addAll(statisticResolver.resolveAllForCurrentHand());
        }

        Set<Player> possiblyOutOfDatePlayers = handHistory.getPlayers();
        // Replace all old elements with the new ones which have updated HUD stats
        possiblyOutOfDatePlayers.removeAll(affectedPlayers);
        possiblyOutOfDatePlayers.addAll(affectedPlayers);
    }

    private List<StatisticResolver> loadHudUpdaters() {
        List<StatisticResolver> statisticResolvers = new ArrayList<>();

        Iterator<StatisticResolver> resolverIterator = ServiceLoader.load(
                StatisticResolver.class,
                this.getClass().getClassLoader()
        ).iterator();

        while ( resolverIterator.hasNext() ) {
            StatisticResolver statisticResolver = resolverIterator.next();
            statisticResolvers.add(statisticResolver);
        }

        return statisticResolvers;
    }

    private HudStats.Properties defaultHudStatsConfiguration() {
        HudStats.Properties properties = new HudStats.Properties();
        properties.setPlayerRange(HudStats.Properties.PlayerRange.ANY);
        return properties;
    }

    private boolean isConfigurationAcceptsHandHistory(HandHistory handHistory) {
        return hudStatsFilter.getPlayerRange().getApplicableRange().contains(handHistory.getPlayerCount());
    }
}
