package de.lyriaserver.kartenspiele.classes;

import de.lyriaserver.kartenspiele.gui.GameScreen;
import org.bukkit.entity.HumanEntity;

public class Spectator {
    protected final HumanEntity minecraftPlayer;
    protected GameScreen<?> screen;

    public Spectator(HumanEntity player) {
        this.minecraftPlayer = player;
    }

    public GameScreen<?> getScreen() {
        return screen;
    }

    public void openScreen(GameScreen<?> screen) {
        this.screen = screen;
        minecraftPlayer.openInventory(screen.getInventory());
    }

    /**
     * Sends a message to this player that is formatted using {@link String#format(String, Object...)}
     * @param message The message to send
     * @param format The parameters passed to {@link String#format(String, Object...)}
     */
    public void sendMessage(String message, Object... format) {
        minecraftPlayer.sendMessage(String.format(message, format));
    }

    public HumanEntity getMcPlayer() {
        return minecraftPlayer;
    }
}
