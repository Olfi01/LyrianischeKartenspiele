package de.lyriaserver.kartenspiele.gui.buttons;

import de.lyriaserver.kartenspiele.classes.Game;
import de.lyriaserver.kartenspiele.classes.Player;
import de.lyriaserver.kartenspiele.gui.GameScreen;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.janboerman.guilib.api.ItemBuilder;
import xyz.janboerman.guilib.api.menu.ItemButton;

import java.util.Arrays;
import java.util.stream.Collectors;

public class OpponentInfo<S extends GameScreen<?>> extends ItemButton<S> {
    private final ItemStack icon;
    private final Player player;
    private final Game<?> game;
    public OpponentInfo(Game<?> game, Player player) {
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
