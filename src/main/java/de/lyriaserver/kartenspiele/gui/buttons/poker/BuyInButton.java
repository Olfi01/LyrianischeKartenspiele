package de.lyriaserver.kartenspiele.gui.buttons.poker;

import de.lyriaserver.kartenspiele.constants.Sounds;
import de.lyriaserver.kartenspiele.games.Poker;
import de.lyriaserver.kartenspiele.gui.screens.PokerScreen;
import de.lyriaserver.kartenspiele.players.PokerPlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import xyz.janboerman.guilib.api.menu.ItemButton;

import java.util.Optional;

public class BuyInButton extends ItemButton<PokerScreen> {
    private final Poker game;

    public BuyInButton(Poker game) {
        this.game = game;
    }

    @Override
    public void onClick(PokerScreen holder, InventoryClickEvent event) {
        Optional<PokerPlayer> player = game.tryBuyIn(event.getWhoClicked());
        if (player.isPresent()) {
            holder.setPlayer(player.get());
        }
        else {
            event.getWhoClicked().playSound(Sounds.GENERIC_ERROR);
        }
    }
}
