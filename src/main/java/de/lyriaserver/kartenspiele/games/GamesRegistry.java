package de.lyriaserver.kartenspiele.games;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class GamesRegistry {
    private final Map<Plugin, List<GameOption>> games = new HashMap<>();

    public void registerGame(Plugin plugin, GameOption game) {
        if (!games.containsKey(plugin)) games.put(plugin, new ArrayList<>());
        games.get(plugin).add(game);
    }

    public void unregisterGame(Plugin plugin, GameOption game) {
        if (!games.containsKey(plugin)) return;
        games.get(plugin).remove(game);
    }

    /**
     * Returns a read-only list of registered games.
     * @return A read-only version of the list of registered games
     */
    @Unmodifiable
    public List<GameOption> getGames() {
        return games.values().stream().flatMap(List::stream).toList();
    }

    public record GameOption(Supplier<IGame<?, ?>> gameSupplier, ItemStack icon) {
    }
}
