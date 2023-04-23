package de.lyriaserver.kartenspiele;

import de.lyriaserver.kartenspiele.classes.BlockPos;
import de.lyriaserver.kartenspiele.classes.Game;
import de.lyriaserver.kartenspiele.listeners.PlayerInteractListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class LyrianischeKartenspiele extends JavaPlugin {
    public static final Random RANDOM = new Random();
    public static LyrianischeKartenspiele INSTANCE = null;
    private final Map<BlockPos, Game<?>> games = new HashMap<>();

    @Override
    public void onEnable() {
        INSTANCE = this;

        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Map<BlockPos, Game<?>> getGames() {
        return games;
    }
}
