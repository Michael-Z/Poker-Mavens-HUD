package com.chronpwn.pokermavenshud.domain;

import com.chronpwn.pokermavenshud.repository.listener.HudStatsParentSetter;

import javax.persistence.*;

/**
 * A poker player.
 * @author Armin Naderi
 */
@Entity
@EntityListeners({ HudStatsParentSetter.class })
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    // Can be negative if they have lost chips in total
    private double balance;

    @Transient
    private HudStats hudStats;

    public Player() {
        hudStats = new HudStats();
    }

    public Player(String username) {
        this();
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public HudStats getHudStats() {
        return hudStats;
    }

    public void setHudStats(HudStats hudStats) {
        this.hudStats = hudStats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (getId() != null ? !getId().equals(player.getId()) : player.getId() != null) return false;
        return getUsername().equals(player.getUsername());
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + getUsername().hashCode();
        return result;
    }
}
