package com.chronpwn.pokermavenshud.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Armin Naderi.
 */
@Entity
public class HandHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String rawText;

    /* The below two fields are not reliable to determine whether a hand history is equal to another, as the
     hand number field is relative to the current logged in player (it is not an absolute number) and it is also
     reset when the poker mavens server is reset. */
    private String tableName;
    private String gameId;

    private String gameDescription;
    private LocalDateTime dateRecorded;

    @OneToMany(fetch = FetchType.EAGER)
    private List<PokerAction> pokerActions;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Player> players;

    private int playerCount;

    public HandHistory() {
        this.pokerActions = new ArrayList<>();
        this.players = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PokerAction> getPokerActions() {
        return pokerActions;
    }

    public void setPokerActions(List<PokerAction> pokerActions) {
        this.pokerActions = pokerActions;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public LocalDateTime getDateRecorded() {
        return dateRecorded;
    }

    public void setDateRecorded(LocalDateTime dateRecorded) {
        this.dateRecorded = dateRecorded;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public String getGameDescription() {
        return gameDescription;
    }

    public void setGameDescription(String gameDescription) {
        this.gameDescription = gameDescription;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }
}
