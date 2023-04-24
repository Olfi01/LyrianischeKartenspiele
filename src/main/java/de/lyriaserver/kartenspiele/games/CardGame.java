package de.lyriaserver.kartenspiele.games;

import de.lyriaserver.kartenspiele.classes.Card;
import de.lyriaserver.kartenspiele.classes.Player;

public interface CardGame {
    /**
     * Called when a player clicks on a card. Should handle whatever action is associated with that
     * (probably playing the card), if it's a valid action.
     * @param player The player that clicked on a card
     * @param card The card that the player clicked on
     * @return true if the input was a valid action
     */
    boolean playerUseCard(Player player, Card card);
}