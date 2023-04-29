package de.lyriaserver.kartenspiele.gui.buttons.poker;

import de.lyriaserver.kartenspiele.constants.Sounds;
import de.lyriaserver.kartenspiele.games.Poker;
import de.lyriaserver.kartenspiele.gui.screens.PokerScreen;
import de.lyriaserver.kartenspiele.players.PokerPlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import xyz.janboerman.guilib.api.menu.ItemButton;

public class BuyOutButton extends ItemButton<PokerScreen> {

    private final Poker game;
    private final PokerPlayer player;

    public BuyOutButton(Poker game, PokerPlayer player) {
        this.game = game;
        this.player = player;
    }

    @Override
    public void onClick(PokerScreen holder, InventoryClickEvent event) {
        if (game.tryBuyOut(player)) {
            holder.setSpectator();
        }
        else {
            player.playSound(Sounds.GENERIC_ERROR);
        }
    }
}
