package com.chronpwn.pokermavenshud.browser;

import com.chronpwn.pokermavenshud.domain.HandHistory;
import com.chronpwn.pokermavenshud.domain.Player;
import com.chronpwn.pokermavenshud.domain.PokerAction;
import com.chronpwn.pokermavenshud.repository.PokerActionRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses serialized hand history statements into deserialized objects.
 * @author Armin Naderi.
 */
@Component
class StatementParser {
    private static final String HAND_META_REGEX = "Hand[ ]{1}#[\\d]+-[\\d]+[ ]{1}-[ ]{1}(?<date>.+)";
    private static final String SEAT_REGEX = "Seat[ ]{1}(?<seat>[\\d]{1}):[ ]{1}(?<name>[a-zA-Z0-9_]+)[ ]{1}\\((?<stack>[\\d]+)\\)";

    private PlayerCache playerCache;
    private PokerActionRepository pokerActionRepository;

    private PokerAction.Street currentStreet;
    private int currentActionOrdinal;

    public StatementParser(PlayerCache playerCache, PokerActionRepository pokerActionRepository) {
        this.playerCache = playerCache;
        this.pokerActionRepository = pokerActionRepository;
    }

    /**
     * Attempts to add a {@link PokerAction} to a {@link HandHistory} if the statement can be deserialized into a
     * poker action.
     *
     * @param handHistory the hand history to attempt adding a poker action to
     * @param serializedStatement the statement that may or may not be deserialized as a poker action
     */
    void attemptPokerActionAddition(HandHistory handHistory, String serializedStatement) {
        /* Only one or none of the below parser methods will cause a change in data, thus it is fine to
             put them together */
        PokerAction.Street street = this.parseStreet(serializedStatement);
        if (street != null) {
            currentStreet = street;
        }
        PokerAction pokerAction = this.parsePokerAction(serializedStatement);
        if (pokerAction != null) {
            pokerActionRepository.save(pokerAction);
            handHistory.getPokerActions().add(pokerAction);
            currentActionOrdinal += 1;
        }
    }

    /**
     * @return a {@link HandHistory} with header fields set
     */
    HandHistory parseHandHistoryHeaders(String[] statements) {
        HandHistory newHand = new HandHistory();
        Set<Player> playersAtStart = newHand.getPlayers();

        // The first statement is the hand meta information
        Pattern pattern = Pattern.compile(HAND_META_REGEX);
        Matcher matcher = pattern.matcher(statements[0]);
        if (matcher.matches()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            newHand.setDateRecorded(LocalDateTime.parse(matcher.group("date"), formatter));
        }

        pattern = Pattern.compile(SEAT_REGEX);
        for (String statement : statements) {
            matcher = pattern.matcher(statement);
            if (matcher.matches()) {
                String username = matcher.group("name");
                Player player = playerCache.findOrCreatePlayer(username);
                playersAtStart.add(player);
            }
        }

        return newHand;
    }

    void resetData() {
        currentStreet = PokerAction.Street.PRE_FLOP;
        currentActionOrdinal = 0;
    }

    private PokerAction.Street parseStreet(String serialized) {
        for (PokerAction.Street street : PokerAction.Street.values()) {
            Pattern pattern = Pattern.compile(street.getRegex());
            // NOTE: Is is IMPERATIVE that the difference is trimmed, otherwise the regex will not work!
            Matcher matcher = pattern.matcher(serialized.trim());
            if (matcher.matches()) {
                return street;
            }
        }
        return null;
    }


    private PokerAction parsePokerAction(String serialized) {
        for (PokerAction.Type type : PokerAction.Type.values()) {
            Pattern pattern = Pattern.compile(type.getRegex());
            // NOTE: Is is IMPERATIVE that the serialized representation is trimmed, otherwise the regex will not work!
            Matcher matcher = pattern.matcher(serialized.trim());
            if (matcher.matches()) {

                String username = matcher.group("name");
                Player player = playerCache.findOrCreatePlayer(username);

                switch (type) {
                    case CHECK: return discretePokerAction(player, type);
                    case FOLD: return discretePokerAction(player, type);
                    case CALL: return continuousPokerAction(player, type, matcher);
                    case RAISE: return continuousPokerAction(player, type, matcher);
                    case BET: return continuousPokerAction(player, type, matcher);
                    default: throw new IllegalArgumentException("Programming error, must be one of the above cases!");
                }
            }
        }
        return null;
    }

    private PokerAction continuousPokerAction(Player player, PokerAction.Type type, Matcher matcher) {
        return new PokerAction(player, type, currentStreet, Float.valueOf(matcher.group("amount")), currentActionOrdinal);
    }

    private PokerAction discretePokerAction(Player player, PokerAction.Type type) {
        return new PokerAction(player, type, currentStreet, 0f, currentActionOrdinal);
    }
}
