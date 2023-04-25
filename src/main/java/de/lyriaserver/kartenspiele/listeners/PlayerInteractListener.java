package de.lyriaserver.kartenspiele.listeners;

import de.lyriaserver.kartenspiele.LyrianischeKartenspiele;
import de.lyriaserver.kartenspiele.classes.BlockPos;
import de.lyriaserver.kartenspiele.classes.Game;
import de.lyriaserver.kartenspiele.gui.screens.ChooseGameMenu;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Map;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || event.getHand() != EquipmentSlot.HAND) return;
        Block block = event.getClickedBlock();
        if (block.getType() != Material.PINK_CARPET) return;
        Player player = event.getPlayer();
        BlockPos position = new BlockPos(block);
        Map<BlockPos, Game<?, ?>> games = LyrianischeKartenspiele.INSTANCE.getGames();
        if (games.containsKey(position)) {
            games.get(position).playerJoin(player);
        }
        else {
            player.openInventory(new ChooseGameMenu(position).getInventory());
        }
    }
}
