package de.lyriaserver.kartenspiele.gui.buttons;

import de.lyriaserver.kartenspiele.games.Game;
import de.lyriaserver.kartenspiele.games.StackGame;
import de.lyriaserver.kartenspiele.games.TurnBasedGame;
import de.lyriaserver.kartenspiele.gui.screens.GameScreen;
import de.lyriaserver.kartenspiele.players.CardGamePlayer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import xyz.janboerman.guilib.api.ItemBuilder;
import xyz.janboerman.guilib.api.menu.ItemButton;

public class DrawButton<G extends Game<G, ?> & StackGame, S extends GameScreen<?, ?>>
        extends ItemButton<S> {
    private static final ItemStack icon =
            new ItemBuilder(Material.PAPER)
                    .name("Karte ziehen")
                    .addLore("Gegenstand: Verdeckte Karte")
                    .build();
    private final G game;
    private final CardGamePlayer player;

    public DrawButton(G game, @Nullable CardGamePlayer player) {
        super(icon);
        this.game = game;
        this.player = player;
    }

    @Override
    public void onClick(S holder, InventoryClickEvent event) {
        if (player == null || game.getStatus() != Game.Status.Started) return;
        player.drawCard(game.getStack());
        game.broadcastMessage("%s zieht eine Karte.", player.getName());
        if (game instanceof TurnBasedGame<?, ?> turnBasedGame)
            turnBasedGame.nextTurn();
        game.updatePlayerScreens();
    }
}
