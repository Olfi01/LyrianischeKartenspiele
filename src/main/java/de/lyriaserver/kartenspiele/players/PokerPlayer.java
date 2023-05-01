package de.lyriaserver.kartenspiele.players;

import org.bukkit.entity.HumanEntity;

public class PokerPlayer extends CardGamePlayer implements MoneyPlayer {
    private int chips;
    private boolean hasRaised = false;
    private boolean hasChecked = false;

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

    public boolean hasRaised() {
        return hasRaised;
    }
    public void setHasRaised(boolean hasRaised) {
        this.hasRaised = hasRaised;
    }

    public boolean hasChecked() {
        return hasChecked;
    }

    public void setHasChecked(boolean hasChecked) {
        this.hasChecked = hasChecked;
    }

    public boolean hasFolded() {
        return hand.isEmpty();
    }
}
