package de.lyriaserver.kartenspiele.gui.buttons;

import de.lyriaserver.kartenspiele.classes.Player;
import de.lyriaserver.kartenspiele.gui.LobbyScreen;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import xyz.janboerman.guilib.api.ItemBuilder;
import xyz.janboerman.guilib.api.menu.ItemButton;

public class LobbyPlayerButton extends ItemButton<LobbyScreen<?>> {
    private final ItemStack readyIcon;
    private final ItemStack notReadyIcon;
    public LobbyPlayerButton(Player player) {
        readyIcon = new ItemBuilder(Material.PLAYER_HEAD)
                .name(player.getName())
                .lore("Bereit")
                .changeMeta((SkullMeta meta) -> meta.setOwningPlayer((OfflinePlayer) player.getMcPlayer()))
                .build();
        readyIcon.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        readyIcon.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        notReadyIcon = new ItemBuilder(Material.PLAYER_HEAD)
                .name(player.getName())
                .lore("Nicht bereit")
                .changeMeta((SkullMeta meta) -> meta.setOwningPlayer((OfflinePlayer) player.getMcPlayer()))
                .build();
        setIcon(notReadyIcon);
    }

    public void setReady(boolean ready) {
        setIcon(ready ? readyIcon : notReadyIcon);
    }
}
