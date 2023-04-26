package de.lyriaserver.kartenspiele;

import de.lyriaserver.kartenspiele.classes.BlockPos;
import de.lyriaserver.kartenspiele.games.Game;
import de.lyriaserver.kartenspiele.games.GamesRegistry;
import de.lyriaserver.kartenspiele.games.MauMau;
import de.lyriaserver.kartenspiele.listeners.PlayerInteractListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class LyrianischeKartenspiele extends JavaPlugin {
    public static final Random RANDOM = new Random();
    public static LyrianischeKartenspiele INSTANCE = null;
    private final Map<BlockPos, Game<?, ?>> games = new HashMap<>();
    private final GamesRegistry gamesRegistry = new GamesRegistry();

    @Override
    public void onEnable() {
        INSTANCE = this;

        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);

        registerGames();
    }

    private void registerGames() {
        gamesRegistry.registerGame(this, new GamesRegistry.GameOption(MauMau::new, MauMau.ICON));
    }

    public Map<BlockPos, Game<?, ?>> getGames() {
        return games;
    }

    public GamesRegistry getGamesRegistry() {
        return gamesRegistry;
    }
}
