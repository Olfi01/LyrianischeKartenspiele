package de.lyriaserver.kartenspiele.gui.screens;

import de.lyriaserver.kartenspiele.games.Poker;
import de.lyriaserver.kartenspiele.gui.buttons.CancelGameButton;
import de.lyriaserver.kartenspiele.gui.buttons.CardView;
import de.lyriaserver.kartenspiele.gui.buttons.IntView;
import de.lyriaserver.kartenspiele.players.PokerPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import xyz.janboerman.guilib.api.ItemBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PokerScreen extends GameScreen<Poker, PokerPlayer> {
    private static final ItemStack ownMoneyIcon =
            new ItemBuilder(Material.GOLD_NUGGET)
                    .name("Eigene Chips:")
                    .build();
    private final CardView[] cards = new CardView[7];
    private final List<IntView> intViews = new ArrayList<>();
    public PokerScreen(Poker game, @Nullable PokerPlayer player) {
        super(game, 54, player);

        // TODO: buy-in button in slot 0?
        for (int i = 0; i < 5; i++) {
            final int finalI = i;
            CardView button = new CardView(() -> game.getCenterCard(finalI));
            cards[i] = button;
            setButton(2 + i, button);
        }
        setButton(8, new CancelGameButton(game));

        if (player != null) {
            IntView button = new IntView(ownMoneyIcon, player::getChips);
            intViews.add(button);
            setButton(18, button);
        }
        // TODO: fold, less, amount, more, raise/call, buy-out

        // TODO: card combination value?

        if (player != null) {
            for (int i = 0; i < 2; i++) {
                final int finalI = i;
                CardView button = new CardView(() -> Optional.of(player.getCards().get(finalI)));
                cards[5 + i] = button;
                setButton(2 * i + 30, button);
            }
        }

        // TODO: setButton(34, topBetButton);

        // TODO: fill
    }

    @Override
    public void update() {
        for (CardView card : cards) {
            if (card != null) card.update();
        }
        for (IntView view : intViews) {
            view.update();
        }

        // TODO: fill
    }

    public void setSpectator() {

    }
}