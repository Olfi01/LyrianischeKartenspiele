package de.lyriaserver.kartenspiele.gui.buttons;

import de.lyriaserver.kartenspiele.constants.Sounds;
import de.lyriaserver.kartenspiele.games.GameStatus;
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

public class DrawCardButton extends ItemButton<GameScreen<?, ?>> {
    private static final ItemStack icon =
            new ItemBuilder(Material.PAPER)
                    .name("Karte ziehen")
                    .addLore("Gegenstand: Verdeckte Karte")
                    .build();
    private final StackGame<?, ?> game;
    private final CardGamePlayer player;

    public DrawCardButton(StackGame<?, ?> game, @Nullable CardGamePlayer player) {
        super(icon);
        this.game = game;
        this.player = player;
    }

    @Override
    public void onClick(GameScreen<?, ?> holder, InventoryClickEvent event) {
        if (player == null || game.getStatus() != GameStatus.Started) return;
        player.drawCard(game.getStack());
        game.broadcastSound(Sounds.CARD_DRAW);
        game.broadcastMessage("%s zieht eine Karte.", player.getName());
        if (game instanceof TurnBasedGame<?, ?> turnBasedGame)
            turnBasedGame.nextTurn();
        game.updatePlayerScreens();
    }
}
