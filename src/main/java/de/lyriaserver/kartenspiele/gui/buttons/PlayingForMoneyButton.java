package de.lyriaserver.kartenspiele.gui.buttons;

import de.lyriaserver.kartenspiele.LyrianischeKartenspiele;
import de.lyriaserver.kartenspiele.constants.Sounds;
import de.lyriaserver.kartenspiele.games.MoneyGame;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.janboerman.guilib.api.ItemBuilder;
import xyz.janboerman.guilib.api.menu.ItemButton;
import xyz.janboerman.guilib.api.menu.MenuHolder;

public class PlayingForMoneyButton<MH extends MenuHolder<?>> extends ItemButton<MH> {
    private static final ItemStack playingForMoneyIcon =
            new ItemBuilder(Material.GOLD_NUGGET)
                    .name("Spiel um Geld")
                    .build();
    private static final ItemStack playingForFameIcon =
            new ItemBuilder(Material.DRAGON_EGG)
                    .name("Spiel um Ruhm")
                    .build();
    private final MoneyGame<?, ?> game;
    public PlayingForMoneyButton(MoneyGame<?, ?> game) {
        this.game = game;
        update();
    }

    @Override
    public void onClick(MH holder, InventoryClickEvent event) {
        if (!game.isPlayingForMoney() && LyrianischeKartenspiele.getEconomyHelper().isEmpty()) {
            event.getWhoClicked().playSound(Sounds.GENERIC_ERROR);
            return;
        }
        game.setPlayingForMoney(!game.isPlayingForMoney());
        game.setAllPlayersUnready();
        update();
    }

    public void update() {
        if (game.isPlayingForMoney())
            setIcon(playingForMoneyIcon);
        else
            setIcon(playingForFameIcon);
    }
}
