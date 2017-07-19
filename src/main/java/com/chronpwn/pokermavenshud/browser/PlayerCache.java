package com.chronpwn.pokermavenshud.browser;

import com.chronpwn.pokermavenshud.domain.Player;
import com.chronpwn.pokermavenshud.repository.PlayerRepository;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Loads a {@link Player} object in the most efficient way possible.
 * @author Armin Naderi.
 */
@Component
public class PlayerCache {

    private PlayerRepository playerRepository;
    private DataAggregator dataAggregator;

    private Set<Player> playerCache;

    public PlayerCache(PlayerRepository playerRepository, DataAggregator dataAggregator) {
        this.playerRepository = playerRepository;
        this.dataAggregator = dataAggregator;
        this.playerCache = new HashSet<>();
    }

    Player findOrCreatePlayer(String username) {
        /* The cache will be null if a hand has not ended, and it will be empty if the #parseHandHistoryHeaders method
         has not been called due to a hand starting. */
        if (playerCache != null && !playerCache.isEmpty()) {
            // Attempt to use the cache before performing any intensive database operations
            for (Player player : playerCache) {
                if (player.getUsername().equals(username)) {
                    return player;
                }
            }
        }
        Player player = playerRepository.findByUsername(username);
        if (player == null) {
            player = new Player(username);
            playerRepository.save(player);
            // Return quickly to avoid looping through all hand histories for a newly logged player
            return player;
        }
        // Compute the transient HUD stats for this player
        player.setHudStats(dataAggregator.computeHudStats(player));
        // Cache the player to be quickly retrieved when the next hand starts, or when headers need to be parsed
        playerCache.add(player);
        return player;
    }

    /**
     * Computed at the end of each hand, so these entries are more up to date than the current cache.
     *
     * @param playerCache the updated player objects at the end of a hand
     */
    void replaceCacheEntries(Set<Player> playerCache) {
        this.playerCache.removeAll(playerCache);
        this.playerCache.addAll(playerCache);
    }
}