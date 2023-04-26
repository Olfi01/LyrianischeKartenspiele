package de.lyriaserver.kartenspiele.gui.screens;

import de.lyriaserver.kartenspiele.LyrianischeKartenspiele;
import de.lyriaserver.kartenspiele.classes.BlockPos;
import de.lyriaserver.kartenspiele.games.Game;
import de.lyriaserver.kartenspiele.games.GamesRegistry;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import xyz.janboerman.guilib.api.ItemBuilder;
import xyz.janboerman.guilib.api.menu.ItemButton;
import xyz.janboerman.guilib.api.menu.MenuHolder;
import xyz.janboerman.guilib.api.menu.PageMenu;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ChooseGameMenu extends PageMenu<LyrianischeKartenspiele> {
    private static final ItemStack previousPageIcon =
            new ItemBuilder(Material.ARROW)
                    .name("Vorherige Seite")
                    .build();
    private static final ItemStack nextPageIcon =
            new ItemBuilder(Material.ARROW)
                    .name("Nächste Seite")
                    .build();
    private final List<GamesRegistry.GameOption> games;
    private final BlockPos position;
    private final int startIndex;
    public ChooseGameMenu(BlockPos position) {
        this(LyrianischeKartenspiele.INSTANCE, position);
    }
    public ChooseGameMenu(LyrianischeKartenspiele plugin, BlockPos position) {
        this(plugin, position, 0);
    }

    public ChooseGameMenu(LyrianischeKartenspiele plugin, BlockPos position, int startIndex) {
        super(plugin, new MenuHolder<>(plugin, 27), "Spiele", null, null, previousPageIcon, nextPageIcon);
        this.position = position;
        this.startIndex = startIndex;
        this.games = LyrianischeKartenspiele.INSTANCE.getGamesRegistry().getGames();
    }

    @Override
    public MenuHolder<?> getPage() {
        if (!(super.getPage() instanceof MenuHolder<?> menu)) throw new RuntimeException();
        return menu;
    }

    @Override
    public Optional<? extends Supplier<? extends PageMenu<LyrianischeKartenspiele>>> getNextPageMenu() {
        if (startIndex + getPageSize() < games.size()) {
            return Optional.of(() -> new ChooseGameMenu(getPlugin(), position, startIndex + getPageSize()));
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<? extends Supplier<? extends PageMenu<LyrianischeKartenspiele>>> getPreviousPageMenu() {
        if (startIndex > 0) {
            return Optional.of(() -> new ChooseGameMenu(getPlugin(), position, Math.max(0, startIndex - getPageSize())));
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent openEvent) {
        for (int slot = 0; slot < getPageSize() && startIndex + slot < games.size(); slot++) {
            getPage().setButton(slot, new ChooseGameButton(games.get(startIndex + slot)));
        }

        super.onOpen(openEvent);
    }

    public class ChooseGameButton extends ItemButton<MenuHolder<LyrianischeKartenspiele>> {
        private final Supplier<Game<?, ?>> gameSupplier;
        public ChooseGameButton(GamesRegistry.GameOption gameOption) {
            super(gameOption.icon());
            this.gameSupplier = gameOption.gameSupplier();
        }

        @Override
        public void onClick(MenuHolder<LyrianischeKartenspiele> holder, InventoryClickEvent event) {
            Map<BlockPos, Game<?, ?>> games = LyrianischeKartenspiele.INSTANCE.getGames();
            HumanEntity player = event.getWhoClicked();
            if (!games.containsKey(position)) {
                Game<?, ?> game = gameSupplier.get();
                games.put(position, game);
                game.playerJoin(player);
            }
            else {
                player.closeInventory();
                player.sendMessage("Hier läuft bereits ein Spiel!");
            }
        }
    }
}
