package de.lyriaserver.kartenspiele.gui.buttons;

import de.lyriaserver.kartenspiele.classes.Updatable;
import de.lyriaserver.kartenspiele.gui.screens.GameScreen;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import xyz.janboerman.guilib.api.menu.ItemButton;

import java.util.Collections;
import java.util.function.IntSupplier;

public class IntView extends ItemButton<GameScreen<?, ?>> implements Updatable {
    private final ItemStack icon;
    private final IntSupplier supplier;

    /**
     * Creates a new IntView button that can be updated to reflect an updating value.
     * @param icon The icon to use for this button. The lore will be overridden.
     * @param supplier The supplier that always returns the updated int value
     */
    public IntView(ItemStack icon, IntSupplier supplier) {
        this.icon = icon;
        this.supplier = supplier;
        update();
    }

    public void update() {
        icon.lore(Collections.singletonList(Component.text(supplier.getAsInt())));
        setIcon(icon);
    }
}
