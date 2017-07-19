package com.chronpwn.pokermavenshud.component;

import com.chronpwn.pokermavenshud.domain.HudStats;
import com.chronpwn.pokermavenshud.domain.Player;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.util.Objects;

/**
 * A ListView cell representing a player and their statistics.
 * @author Armin Naderi.
 */
public class PlayerCellFactory implements Callback<ListView<Player>, ListCell<Player>> {
    @Override
    public ListCell<Player> call(ListView<Player> param) {
        return new ListCell<Player>() {

            @Override
            protected void updateItem(Player player, boolean isCellEmpty) {
                super.updateItem(player, isCellEmpty);

                if (Objects.isNull(player) || isCellEmpty) {
                    setGraphic(null);
                    setText("");
                    return;
                }

                setText(formatString(player));
            }
        };
    }

    private String formatString(Player player) {
        HudStats hudStats = player.getHudStats();
        String format = "%s - %5.2f / %5.2f / %5.2f / %5.2f | %d";
        return String.format(format, player.getUsername(),
                hudStats.getVpip().getValue() * 100,
                hudStats.getPfr().getValue() * 100,
                hudStats.getPf3bet().getValue() * 100,
                hudStats.getFold3bet().getValue() * 100,
                hudStats.getSampleSize());
    }
}
