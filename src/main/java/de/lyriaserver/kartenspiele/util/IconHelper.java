package de.lyriaserver.kartenspiele.util;

import de.lyriaserver.kartenspiele.classes.cardgames.Card;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.janboerman.guilib.api.ItemBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * This class prepares the icons for all cards, so the objects don't have to be instantiated anew every time the icon
 * is called
 */
public class IconHelper {
    private static final Map<Card, ItemStack> ICONS = generateIcons();

    @NotNull
    private static Map<Card, ItemStack> generateIcons() {
        Map<Card, ItemStack> icons = new HashMap<>();
        for (Card.Color color : Card.Color.values()) {
            for (Card.Value value : Card.Value.values()) {
                Card card = new Card(color, value);
                icons.put(card, new ItemBuilder(Material.PAPER)
                        .name(card.getName())
                        .addLore(card.getItemLore())
                        .build());
            }
        }
        return icons;
    }

    public static ItemStack getIcon(Card card) {
        return ICONS.get(card);
    }
}
