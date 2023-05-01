package de.lyriaserver.kartenspiele.gui.buttons.poker;

import de.lyriaserver.kartenspiele.classes.Updatable;
import de.lyriaserver.kartenspiele.constants.Sounds;
import de.lyriaserver.kartenspiele.games.Poker;
import de.lyriaserver.kartenspiele.gui.buttons.AmountButton;
import de.lyriaserver.kartenspiele.gui.screens.PokerScreen;
import de.lyriaserver.kartenspiele.players.PokerPlayer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import xyz.janboerman.guilib.api.ItemBuilder;
import xyz.janboerman.guilib.api.menu.ItemButton;

public class RaiseCallButton extends ItemButton<PokerScreen> implements Updatable {
    private static final ItemStack callIcon =
            new ItemBuilder(Material.GREEN_WOOL)
                    .name("Mitgehen")
                    .build();
    private static final ItemStack raiseIcon =
            new ItemBuilder(Material.YELLOW_WOOL)
                    .name("Erhöhen")
                    .build();
    private static final ItemStack allInIcon =
            new ItemBuilder(Material.RED_WOOL)
                    .name("Alles setzen")
                    .build();
    private final Poker game;
    private final PokerPlayer player;
    private final AmountButton amountButton;

    public RaiseCallButton(Poker game, PokerPlayer player, AmountButton amountButton) {
        this.game = game;
        this.player = player;
        this.amountButton = amountButton;
        update();
    }

    @Override
    public void onClick(PokerScreen holder, InventoryClickEvent event) {
        if (!game.placeBet(player, amountButton.getAmount())) {
            player.playSound(Sounds.GENERIC_ERROR);
        }
    }

    public void update() {
        if (amountButton.getAmount() == player.getChips()) setIcon(allInIcon);
        else if (amountButton.getAmount() == game.playerCallAmount(player)) setIcon(callIcon);
        else setIcon(raiseIcon);
    }
}
