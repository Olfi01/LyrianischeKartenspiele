package de.lyriaserver.kartenspiele;

import de.lyriaserver.kartenspiele.classes.BlockPos;
import de.lyriaserver.kartenspiele.games.*;
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

    @Override
    public void onDisable() {
        games.forEach((pos, game) -> {
            if (game instanceof MoneyGame<?,?> moneyGame && moneyGame.isPlayingForMoney()) {
                moneyGame.onGameAbort();
            }
        });
    }

    private void registerGames() {
        gamesRegistry.registerGame(this, new GamesRegistry.GameOption(MauMau::new, MauMau.ICON));
        gamesRegistry.registerGame(this, new GamesRegistry.GameOption(() -> new Poker(500), Poker.ICON_500));
        gamesRegistry.registerGame(this, new GamesRegistry.GameOption(() -> new Poker(1000), Poker.ICON_1000));
        gamesRegistry.registerGame(this, new GamesRegistry.GameOption(() -> new Poker(2000), Poker.ICON_2000));
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
