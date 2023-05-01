package de.lyriaserver.kartenspiele.gui.buttons;

import de.lyriaserver.kartenspiele.LyrianischeKartenspiele;
import de.lyriaserver.kartenspiele.classes.Updatable;
import de.lyriaserver.kartenspiele.constants.Sounds;
import de.lyriaserver.kartenspiele.games.MoneyGame;
import de.lyriaserver.kartenspiele.gui.screens.GameScreen;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.janboerman.guilib.api.ItemBuilder;
import xyz.janboerman.guilib.api.menu.ItemButton;

public class PlayingForMoneyButton extends ItemButton<GameScreen<?,?>> implements Updatable {
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

    public void onClick(GameScreen<?,?> holder, InventoryClickEvent event) {
        if (!game.isPlayingForMoney() && LyrianischeKartenspiele.getEconomyHelper().isEmpty()) {
            event.getWhoClicked().playSound(Sounds.GENERIC_ERROR);
            return;
        }
        game.setPlayingForMoney(!game.isPlayingForMoney());
        game.setAllPlayersUnready();
        game.updatePlayerScreens();
        update();
    }

    public void update() {
        if (game.isPlayingForMoney())
            setIcon(playingForMoneyIcon);
        else
            setIcon(playingForFameIcon);
    }
}
