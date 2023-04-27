package de.lyriaserver.kartenspiele.classes;

import de.lyriaserver.kartenspiele.gui.screens.GameScreen;
import de.lyriaserver.kartenspiele.players.Player;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.HumanEntity;

import java.util.UUID;

public class Spectator {
    protected final HumanEntity minecraftPlayer;
    protected GameScreen<?, ?> screen;

    public Spectator(HumanEntity player) {
        this.minecraftPlayer = player;
    }

    public GameScreen<?, ?> getScreen() {
        return screen;
    }

    /**
     * Opens the given game screen for this player, saving a reference to return in {@link Spectator#getScreen()}.
     * @param screen The screen to open
     */
    public void openScreen(GameScreen<?, ?> screen) {
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

    /**
     * Plays the given sound for this spectator / player
     * @param sound The sound to play. Can be created using {@link Sound#sound(Key, Sound.Source, float, float)}
     */
    public void playSound(Sound sound) {
        minecraftPlayer.playSound(sound);
    }

    /**
     * Returns the {@link HumanEntity} representing the minecraft instance of this player
     * @return the {@link HumanEntity} representing the minecraft instance of this player
     */
    public HumanEntity getMcPlayer() {
        return minecraftPlayer;
    }

    public UUID getUid() {
        return minecraftPlayer.getUniqueId();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Player player)) return false;
        return this.getUid().equals(player.getUid());
    }

    @Override
    public int hashCode() {
        return this.getUid().hashCode();
    }
}
