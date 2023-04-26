package de.lyriaserver.kartenspiele.gui.buttons;

import de.lyriaserver.kartenspiele.games.Game;
import de.lyriaserver.kartenspiele.gui.screens.GameScreen;
import de.lyriaserver.kartenspiele.players.Player;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.janboerman.guilib.api.ItemBuilder;
import xyz.janboerman.guilib.api.menu.ItemButton;

import java.util.Arrays;
import java.util.stream.Collectors;

public class OpponentInfo<S extends GameScreen<?, ?>, P extends Player> extends ItemButton<S> {
    private final ItemStack icon;
    private final P player;
    private final Game<?, P> game;
    public OpponentInfo(Game<?, P> game, P player) {
        this.game = game;
        this.player = player;
        icon = new ItemBuilder(Material.PLAYER_HEAD)
                .name(player.getName())
                .changeMeta((SkullMeta meta) -> meta.setOwningPlayer((OfflinePlayer) player.getMcPlayer()))
                .lore(game.getOpponentLore(player))
                .build();
        setIcon(icon);
    }

    public void update() {
        icon.lore(Arrays.stream(game.getOpponentLore(player)).map(Component::text).collect(Collectors.toList()));
        setIcon(icon);
    }
}
