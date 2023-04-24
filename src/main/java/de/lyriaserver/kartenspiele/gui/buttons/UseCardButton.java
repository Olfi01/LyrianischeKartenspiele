package de.lyriaserver.kartenspiele.gui.buttons;

import de.lyriaserver.kartenspiele.classes.Card;
import de.lyriaserver.kartenspiele.classes.Player;
import de.lyriaserver.kartenspiele.games.CardGame;
import de.lyriaserver.kartenspiele.gui.GameScreen;
import de.lyriaserver.kartenspiele.util.IconHelper;
import org.bukkit.event.inventory.InventoryClickEvent;
import xyz.janboerman.guilib.api.menu.ItemButton;

public class UseCardButton extends ItemButton<GameScreen<?>> {
    private final CardGame game;
    private final Player player;
    private final Card card;

    public UseCardButton(Card card, CardGame game, Player player) {
        super(IconHelper.getIcon(card));
        this.card = card;
        this.game = game;
        this.player = player;
    }

    @Override
    public void onClick(GameScreen<?> holder, InventoryClickEvent event) {
        game.playerUseCard(player, card);
    }
}
