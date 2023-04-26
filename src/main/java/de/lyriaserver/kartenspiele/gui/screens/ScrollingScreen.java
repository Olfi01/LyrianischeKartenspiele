package de.lyriaserver.kartenspiele.gui.screens;

import de.lyriaserver.kartenspiele.games.Game;
import de.lyriaserver.kartenspiele.players.Player;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import xyz.janboerman.guilib.api.ItemBuilder;
import xyz.janboerman.guilib.api.menu.ItemButton;

/**
 * Provides a base for screens using a scrolling feature. To use, extend this class and put the two scrolling buttons
 * anywhere in the GUI ({@code setButton(YOUR_SLOT, scrollUpButton)}).
 * See an example in {@link MauMauScreen}
 */
public abstract class ScrollingScreen<G extends Game<G, P>, P extends Player> extends GameScreen<G, P> {
    protected int scrollingOffset = 0;
    protected final ScrollButton scrollUpButton;
    protected final ScrollButton scrollDownButton;
    public ScrollingScreen(G game, int size, @Nullable P player) {
        this(game, size, player, 1, -1);
    }

    public ScrollingScreen(G game, int size, @Nullable P player, int scrollDownOffset, int scrollUpOffset) {
        super(game, size, player);
        scrollUpButton = new ScrollButton(scrollUpOffset);
        scrollDownButton = new ScrollButton(scrollDownOffset);
    }

    /**
     * Scrolls the screen by the given amount
     * @param offset The amount to scroll by
     * @return true if the offset has changed
     */
    public boolean scroll(int offset) {
        int oldOffset = scrollingOffset;
        scrollingOffset += offset;
        int maxOffset = getMaxOffset();
        if (maxOffset >= 0 && scrollingOffset > maxOffset) scrollingOffset = maxOffset;
        else if (scrollingOffset < 0) scrollingOffset = 0;
        return scrollingOffset != oldOffset;
    }

    /**
     * This method is called when the scrolling offset has changed. It should update the buttons affected by scrolling.
     */
    public abstract void updateView();

    /**
     * Returns the maximum offset for this screen. Set to a negative value to disable.
     * @return the maximum offset for this screen or a negative value to disable.
     */
    public abstract int getMaxOffset();

    public class ScrollButton extends ItemButton<ScrollingScreen<G, P>> {
        private final int offset;
        private static final ItemStack scrollUpIcon =
                new ItemBuilder(Material.LADDER)
                        .name("Hoch")
                        .build();
        private static final ItemStack scrollDownIcon =
                new ItemBuilder(Material.POINTED_DRIPSTONE)
                        .name("Runter")
                        .build();
        public ScrollButton(int offset) {
            super(offset < 0 ? scrollUpIcon : scrollDownIcon);
            this.offset = offset;
        }

        @Override
        public void onClick(ScrollingScreen<G, P> holder, InventoryClickEvent event) {
            if (scroll(offset)) updateView();
        }
    }
}
