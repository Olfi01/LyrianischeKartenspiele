package de.lyriaserver.kartenspiele.classes.poker;

import de.lyriaserver.kartenspiele.players.MoneyPlayer;

import java.util.*;

/**
 * Models the pot to hold the bets in a game of poker. Can only be deposited into or reset to zero by taking the entire
 * pot.
 */
public class Pot {
    private final Map<MoneyPlayer, Integer> bets = new HashMap<>();

    /**
     * Deposits the given amount of chips into the pot.
     * @param amount The amount of chips to bet
     * @return false if the player hasn't got enough money
     * @throws IllegalArgumentException if the amount isn't enough to match the current highest bet TODO: implement
     */
    public boolean deposit(MoneyPlayer player, int amount) {
        if (player.getChips() < amount) return false;
        putBet(player, amount);
        player.subtractChips(amount);
        return true;
    }

    /**
     * Splits the chips in the pot into equal parts for all given players, leaving just the remainder in the pot.
     * Players that didn't bet on this pot will be ignored.
     * @param winners A collection of players among whom the pot should be split
     */
    public void split(Collection<MoneyPlayer> winners) {
        List<MoneyPlayer> filteredWinners = winners.stream().filter(bets::containsKey).toList();
        int sum = sum();
        int winningsPerPlayer = sum / filteredWinners.size();
        int remainder = sum % filteredWinners.size();
        for (MoneyPlayer player : filteredWinners) {
            player.addChips(winningsPerPlayer);
        }
        bets.clear();
        bets.put(null, remainder);
    }

    /**
     * Returns the amount of chips currently in this pot.
     * @return the amount of chips currently in this pot
     */
    public int getBalance() {
        return sum();
    }

    /**
     * Lets the given player go all-in in this pot
     * @param player The player going all-in
     */
    public void allIn(MoneyPlayer player) {
        int amount = player.getChips();
        this.putBet(player, amount);
        player.subtractChips(amount);
    }

    /**
     * Lets the given player collect all their winnings from this pot, returning a side pot in the process,
     * if they didn't win everything in this pot. A winner for that side pot then needs to be evaluated.
     * @param player The player who won this pot.
     * @return A side pot with the remaining bets, or {@link Optional#empty()} if the player won everything.
     */
    public Optional<Pot> collectWinnings(MoneyPlayer player) {
        Pot sidePot = null;
        if (!bets.containsKey(player)) return Optional.of(this);
        int winnersBet = bets.get(player);
        if (winnersBet < getMaxBet()) {
            sidePot = new Pot();
            for (Map.Entry<MoneyPlayer, Integer> entry : bets.entrySet()) {
                if (entry.getValue() > winnersBet) {
                    int difference = entry.getValue() - winnersBet;
                    entry.setValue(winnersBet);
                    sidePot.putBet(entry.getKey(), difference);
                }
            }
        }
        player.addChips(sum());
        bets.clear();
        return Optional.ofNullable(sidePot);
    }

    /**
     * Returns the maximum bet in this pot, or 0 if no bets have been placed.
     * Remainders in the pot that haven't been bet by a player are ignored.
     * @return the maximum bet in this pot, or 0 if no bets have been placed.
     */
    public int getMaxBet() {
        return bets.entrySet().stream().filter(entry -> entry.getKey() != null)
                .mapToInt(Map.Entry::getValue)
                .max().orElse(0);
    }

    /**
     * Returns the amount of chips that this player needs to bet to match the highest bet
     * @param player The player in question
     * @return the amount of chips that this player needs to bet to match the highest bet
     */
    public int playerCallAmount(MoneyPlayer player) {
        return getMaxBet() - bets.getOrDefault(player, 0);
    }

    /**
     * Returns a set of all players who bet on this pot so far. The resulting set is not linked to the pot and can
     * be modified without side effects.
     * @return a set of all players who bet on this pot so far
     */
    public Set<MoneyPlayer> bettingPlayers() {
        return new HashSet<>(bets.keySet());
    }

    private void putBet(MoneyPlayer player, int amount) {
        if (!bets.containsKey(player)) bets.put(player, amount);
        else bets.put(player, bets.get(player) + amount);
    }

    private int sum() {
        return bets.values().stream().mapToInt(Integer::intValue).sum();
    }
}
