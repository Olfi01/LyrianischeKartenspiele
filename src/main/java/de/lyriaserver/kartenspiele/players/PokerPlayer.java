package de.lyriaserver.kartenspiele.players;

import org.bukkit.entity.HumanEntity;

public class PokerPlayer extends CardGamePlayer implements MoneyPlayer {
    private int chips;

    public PokerPlayer(HumanEntity player, int buyIn) {
        super(player);
        this.chips = buyIn;
    }

    @Override
    public int getChips() {
        return chips;
    }

    @Override
    public void subtractChips(int amount) {
        chips -= amount;
    }

    @Override
    public void addChips(int amount) {
        chips += amount;
    }
}
