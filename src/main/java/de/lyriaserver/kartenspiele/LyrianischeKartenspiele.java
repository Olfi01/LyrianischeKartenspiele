package de.lyriaserver.kartenspiele;

import de.lyriaserver.kartenspiele.classes.BlockPos;
import de.lyriaserver.kartenspiele.games.GamesRegistry;
import de.lyriaserver.kartenspiele.games.IGame;
import de.lyriaserver.kartenspiele.games.MauMau;
import de.lyriaserver.kartenspiele.listeners.PlayerInteractListener;
import de.lyriaserver.kartenspiele.util.EconomyHelper;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public final class LyrianischeKartenspiele extends JavaPlugin {
    public static final Random RANDOM = new Random();
    public static LyrianischeKartenspiele INSTANCE = null;
    @Nullable
    private static Economy economy;
    private final Map<BlockPos, IGame<?, ?>> games = new HashMap<>();
    private final GamesRegistry gamesRegistry = new GamesRegistry();

    @Override
    public void onEnable() {
        INSTANCE = this;
        setupEconomy();

        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);

        registerGames();
    }

    private void registerGames() {
        gamesRegistry.registerGame(this, new GamesRegistry.GameOption(MauMau::new, MauMau.ICON));
    }

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        economy = rsp.getProvider();
    }

    public Map<BlockPos, IGame<?, ?>> getGames() {
        return games;
    }

    public GamesRegistry getGamesRegistry() {
        return gamesRegistry;
    }

    public static Optional<EconomyHelper> getEconomyHelper() {
        return economy == null ? Optional.empty() : Optional.of(new EconomyHelper(economy));
    }
}
