package de.lyriaserver.kartenspiele.util;

import de.lyriaserver.kartenspiele.players.Player;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

public class EconomyHelper {
    private final Economy economy;
    public EconomyHelper(@NotNull Economy economy) {
        this.economy = economy;
    }

    public EconomyResponse withdrawPlayer(Player player, int amount) {
        HumanEntity mcPlayer = player.getMcPlayer();
        if (!(mcPlayer instanceof OfflinePlayer offlinePlayer)) {
            return new EconomyResponse(0, 0,
                    EconomyResponse.ResponseType.FAILURE,
                    "Die Daten zu diesem Spieler konnten nicht gefunden werden.");
        }
        return economy.withdrawPlayer(offlinePlayer, amount);
    }

    public EconomyResponse depositPlayer(Player player, int amount) {
        HumanEntity mcPlayer = player.getMcPlayer();
        if (!(mcPlayer instanceof OfflinePlayer offlinePlayer)) {
            return new EconomyResponse(0, 0,
                    EconomyResponse.ResponseType.FAILURE,
                    "Die Daten zu diesem Spieler konnten nicht gefunden werden.");
        }
        return economy.depositPlayer(offlinePlayer, amount);
    }

    public String format(int amount) {
        return economy.format(amount);
    }
}
