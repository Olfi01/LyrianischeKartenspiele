package de.lyriaserver.kartenspiele.gui.buttons;

import de.lyriaserver.kartenspiele.classes.Game;
import de.lyriaserver.kartenspiele.constants.Permissions;
import de.lyriaserver.kartenspiele.gui.GameScreen;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.janboerman.guilib.api.ItemBuilder;
import xyz.janboerman.guilib.api.menu.ItemButton;

public class CancelGameButton extends ItemButton<GameScreen<?>> {
    private static final ItemStack icon =
            new ItemBuilder(Material.LAVA_BUCKET)
                    .name("Spiel abbrechen")
                    .build();
    private final Game<?> game;

    public CancelGameButton(Game<?> game) {
        super(icon);
        this.game = game;
    }

    @Override
    public void onClick(GameScreen<?> holder, InventoryClickEvent event) {
        if (event.getWhoClicked().hasPermission(Permissions.CANCEL_GAME)) {
            game.broadcastMessage("Das Spiel wurde von %s abgebrochen.", event.getWhoClicked().getName());
            game.getPlayers().forEach(player -> player.getMcPlayer().closeInventory());
            game.getSpectators().forEach(spectator -> spectator.getMcPlayer().closeInventory());
            game.finishGame();
        }
    }
}
