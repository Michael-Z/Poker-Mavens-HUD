package com.chronpwn.pokermavenshud.domain;

import javax.persistence.*;

/**
 * @author Armin Naderi.
 */
@Entity
public class PokerAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    private Type type;
    private Street street;
    private float amount;
    // Represents the order of this action in the entire hand history (zero-indexed)
    private int ordinal;

    public PokerAction() {
    }

    public PokerAction(Player player, Type type, Street street, float amount, int ordinal) {
        this.player = player;
        this.type = type;
        this.street = street;
        this.amount = amount;
        this.ordinal = ordinal;
    }

    private static final String USERNAME_REGEX_PREFIX = "(?<name>[a-zA-Z0-9_]+)";
    private static final String ALL_IN_REGEX_SUFFIX = "([ ]{1}\\(All-in\\))?";

    public enum Type {
        CHECK(USERNAME_REGEX_PREFIX + "[ ]{1}checks"),
        FOLD(USERNAME_REGEX_PREFIX + "[ ]{1}folds"),
        //Extra amount to call
        CALL(USERNAME_REGEX_PREFIX + "[ ]{1}calls[ ]{1}(?<amount>[\\d]+)" + ALL_IN_REGEX_SUFFIX),
        // Amount raised to, not amount increased. Pre-flop opens are considered raises
        RAISE(USERNAME_REGEX_PREFIX + "[ ]{1}raises[ ]{1}to[ ]{1}(?<amount>[\\d]+)" + ALL_IN_REGEX_SUFFIX),
        // This happens on the post-flop when the player with the lead bets onto his/her opponents
        BET(USERNAME_REGEX_PREFIX + "[ ]{1}bets[ ]{1}(?<amount>[\\d]+)" + ALL_IN_REGEX_SUFFIX);

        private String regex;

        Type(String regex) {
            this.regex = regex;
        }

        public String getRegex() {
            return regex;
        }
    }

    private static final String BRACKET_REGEX_SUFFIX = "[ ]{1}\\*\\*[ ]{1}\\[[a-zA-Z0-9 ]+\\]";
    private static final String STAR_REGEX_PREFIX = "\\*\\*[ ]{1}";

    public enum Street {
        PRE_FLOP("Hand[ ]{1}#"),
        FLOP(STAR_REGEX_PREFIX + "Flop" + BRACKET_REGEX_SUFFIX),
        TURN(STAR_REGEX_PREFIX + "Turn" + BRACKET_REGEX_SUFFIX),
        RIVER(STAR_REGEX_PREFIX + "River" + BRACKET_REGEX_SUFFIX);

        private String regex;

        Street(String regex) {
            this.regex = regex;
        }

        public String getRegex() {
            return regex;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Street getStreet() {
        return street;
    }

    public void setStreet(Street street) {
        this.street = street;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }
}
