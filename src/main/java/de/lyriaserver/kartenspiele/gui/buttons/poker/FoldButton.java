package de.lyriaserver.kartenspiele.gui.buttons.poker;

import de.lyriaserver.kartenspiele.games.Poker;
import de.lyriaserver.kartenspiele.gui.screens.PokerScreen;
import de.lyriaserver.kartenspiele.players.PokerPlayer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.janboerman.guilib.api.ItemBuilder;
import xyz.janboerman.guilib.api.menu.ItemButton;

public class FoldButton extends ItemButton<PokerScreen> {
    private static final ItemStack icon =
            new ItemBuilder(Material.BARRIER)
                    .name("Hand aufgeben")
                    .build();
    private final Poker game;
    private final PokerPlayer player;

    public FoldButton(Poker game, @NotNull PokerPlayer player) {
        super(icon);
        this.game = game;
        this.player = player;
    }

    @Override
    public void onClick(PokerScreen holder, InventoryClickEvent event) {
        game.playerFold(player);
    }
}
