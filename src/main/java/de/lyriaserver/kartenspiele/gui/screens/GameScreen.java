package de.lyriaserver.kartenspiele.gui.screens;

import de.lyriaserver.kartenspiele.LyrianischeKartenspiele;
import de.lyriaserver.kartenspiele.games.IGame;
import de.lyriaserver.kartenspiele.players.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.Nullable;
import xyz.janboerman.guilib.api.menu.MenuHolder;

public abstract class GameScreen<G extends IGame<G, P>, P extends Player> extends MenuHolder<LyrianischeKartenspiele> {
    protected final G game;
    @Nullable
    protected final P player;

    /**
     * Creates a new game screen. Sets the basic values usable by the screen.
     * @param game The game to create a screen for
     * @param size The size of the inventory in which the screen is displayed - max. 54
     * @param player The player to associate with this screen, or null if it is a spectator screen
     */
    public GameScreen(G game, int size, @Nullable P player) {
        super(LyrianischeKartenspiele.INSTANCE, size, game.getName());
        this.game = game;
        this.player = player;
    }

    /**
     * This method updates the screen, adding, removing or modifying buttons as necessary to reflect the updated state.
     */
    public abstract void update();

    @Override
    public void onClose(InventoryCloseEvent event) {
        game.playerLeftScreen(player);
    }
}
