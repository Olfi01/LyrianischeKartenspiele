package de.lyriaserver.kartenspiele.gui.buttons;

import de.lyriaserver.kartenspiele.constants.Permissions;
import de.lyriaserver.kartenspiele.constants.Sounds;
import de.lyriaserver.kartenspiele.games.GameStatus;
import de.lyriaserver.kartenspiele.games.IGame;
import de.lyriaserver.kartenspiele.gui.screens.GameScreen;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.janboerman.guilib.api.ItemBuilder;
import xyz.janboerman.guilib.api.menu.ItemButton;

import java.util.List;

public class CancelGameButton extends ItemButton<GameScreen<?, ?>> {
    private static final ItemStack icon =
            new ItemBuilder(Material.LAVA_BUCKET)
                    .name("Spiel abbrechen")
                    .build();
    private final IGame<?, ?> game;

    public CancelGameButton(IGame<?, ?> game) {
        super(icon);
        this.game = game;
    }

    @Override
    public void onClick(GameScreen<?, ?> holder, InventoryClickEvent event) {
        if (canPlayerCancel(event.getWhoClicked())) {
            game.broadcastMessage("Das Spiel wurde von %s abgebrochen.", event.getWhoClicked().getName());
            List.copyOf(game.getPlayers()).forEach(player -> player.getMcPlayer().closeInventory());
            game.getSpectators().forEach(spectator -> spectator.getMcPlayer().closeInventory());
            game.finishGame();
        }
        else {
            event.getWhoClicked().playSound(Sounds.GENERIC_ERROR);
        }
    }

    private boolean canPlayerCancel(HumanEntity player) {
        if (game.getStatus() == GameStatus.Lobby) return player.hasPermission(Permissions.CANCEL_GAME_LOBBY);
        return player.hasPermission(Permissions.CANCEL_GAME);
    }
}
