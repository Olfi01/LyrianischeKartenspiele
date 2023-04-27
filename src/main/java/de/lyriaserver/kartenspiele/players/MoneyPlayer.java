package de.lyriaserver.kartenspiele.players;

public interface MoneyPlayer {
    /**
     * Returns the amount of chips that this player currently has.
     * @return the amount of chips that this player currently has
     */
    int getChips();

    /**
     * Takes the given amount of chips from the player
     * @param amount the amount of chips to take
     */
    void subtractChips(int amount);
    /**
     * Adds the given amount of chips to this player's balance.
     * @param amount The amount of chips to add
     */
    void addChips(int amount);
}
