package de.lyriaserver.kartenspiele.players;

import de.lyriaserver.kartenspiele.classes.Spectator;
import de.lyriaserver.kartenspiele.gui.buttons.LobbyPlayerButton;
import org.bukkit.entity.HumanEntity;

import java.util.UUID;

public class Player extends Spectator {
    private final String name;
    private final LobbyPlayerButton lobbyButton;
    private boolean ready;

    public Player(HumanEntity player) {
        super(player);
        name = player.getName();
        lobbyButton = new LobbyPlayerButton(this);
    }

    public UUID getUid() {
        return minecraftPlayer.getUniqueId();
    }

    public String getName() {
        return name;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
        this.lobbyButton.setReady(ready);
    }

    public boolean isReady() {
        return ready;
    }

    public LobbyPlayerButton getLobbyButton() {
        return lobbyButton;
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
