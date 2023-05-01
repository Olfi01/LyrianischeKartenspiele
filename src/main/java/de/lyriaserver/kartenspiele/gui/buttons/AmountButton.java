package de.lyriaserver.kartenspiele.gui.buttons;

import de.lyriaserver.kartenspiele.classes.Updatable;
import de.lyriaserver.kartenspiele.gui.screens.GameScreen;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.janboerman.guilib.api.ItemBuilder;
import xyz.janboerman.guilib.api.menu.ItemButton;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AmountButton extends ItemButton<GameScreen<?, ?>> implements Updatable {
    private static final List<String> lore = List.of(
            "Linksklick: 1",
            "Rechtsklick: 10",
            "Shift-Linksklick: 100",
            "Shift-Rechtsklick: 1000",
            "Q: 10000"
    );
    private final ItemStack icon;
    private final Set<Updatable> updateListeners = new HashSet<>();
    private int amount;
    private int minAmount;
    private int maxAmount;

    /**
     * Creates a new AmountButton. The amount will be changeble using {@link AmountButton.Add} and
     * {@link AmountButton.Subtract} and will stay in the range of minAmount and maxAmount.
     * @param icon The icon for the button. The lore will be overridden.
     * @param minAmount The minimum amount for this button
     * @param maxAmount The maximum amount for this button
     */
    public AmountButton(ItemStack icon, int minAmount, int maxAmount) {
        super(icon);
        this.icon = icon;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    public void addAmount(int amount) {
        this.amount += amount;
        if (this.amount > maxAmount) this.amount = maxAmount;
        update();
    }

    public void subtractAmount(int amount) {
        this.amount -= amount;
        if (this.amount < minAmount) this.amount = minAmount;
        update();
    }

    public void update() {
        icon.editMeta(meta -> meta.displayName(Component.text(amount)));
        setIcon(icon);
        updateListeners.forEach(Updatable::update);
    }

    public int getAmount() {
        return amount;
    }

    private static int getAmount(ClickType click) {
        return switch (click) {
            case DROP -> 10000;
            case SHIFT_RIGHT -> 1000;
            case SHIFT_LEFT -> 100;
            case RIGHT -> 10;
            default -> 1;
        };
    }

    /**
     * Sets the minimum amount for this button. If minAmount is greater than maxAmount, minAmount will be set to maxAmount.
     * @param minAmount The minimum amount to set for this button.
     */
    public void setMinAmount(int minAmount) {
        if (minAmount > maxAmount) minAmount = maxAmount;
        this.minAmount = minAmount;
        if (amount < minAmount) amount = minAmount;
        update();
    }

    /**
     * Sets the maximum amount for this button. If maxAmount is less than minAmount, minAmount will be set to maxAmount.
     * @param maxAmount The maximum amount to set for this button.
     */
    public void setMaxAmount(int maxAmount) {
        this.maxAmount = maxAmount;
        if (amount > maxAmount) amount = maxAmount;
        if (maxAmount < minAmount) this.minAmount = maxAmount;
        update();
    }

    public void propagateUpdate(Updatable updatable) {
        updateListeners.add(updatable);
    }

    public static class Add extends ItemButton<GameScreen<?, ?>> {
        private static final ItemStack icon =
                new ItemBuilder(Material.WATER_BUCKET)
                        .name("Mehr")
                        .lore(lore)
                        .build();
        private final AmountButton amountButton;

        public Add(AmountButton amountButton) {
            super(icon);
            this.amountButton = amountButton;
        }

        @Override
        public void onClick(GameScreen<?, ?> holder, InventoryClickEvent event) {
            amountButton.addAmount(getAmount(event.getClick()));
        }
    }

    public static class Subtract extends ItemButton<GameScreen<?, ?>> {
        private static final ItemStack icon =
                new ItemBuilder(Material.BUCKET)
                        .name("Weniger")
                        .lore(lore)
                        .build();
        private final AmountButton amountButton;

        public Subtract(AmountButton amountButton) {
            super(icon);
            this.amountButton = amountButton;
        }

        @Override
        public void onClick(GameScreen<?, ?> holder, InventoryClickEvent event) {
            amountButton.subtractAmount(getAmount(event.getClick()));
        }
    }
}
